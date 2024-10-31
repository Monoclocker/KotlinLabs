package lab1
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

fun main(){
    while (true){
        try{
            var firstFigure: Array<Pair<Double, Double>> = emptyArray<Pair<Double, Double>>()
            var secondFigure : Array<Pair<Double, Double>> = emptyArray<Pair<Double, Double>>()

            println("Первый многоугольник")

            while(true){
                println("Добавить точку - 1, перейти к вводу точек второго многоугольника - любая другая кнопка \n" +
                        "Текущее количество точек ${firstFigure.size}")
                val userChoice: String = readln()

                when (userChoice){
                    "1" -> firstFigure += coordinatesInput()
                    else -> {
                        if (firstFigure.size < 3){
                            println("Мало точек для создания многоугольника")
                            continue
                        }
                        break
                    }
                }
            }

            println("Второй многоугольник")

            while(true){
                println("Добавить точку - 1, перейти к рассчётам - любая другая кнопка \n" +
                        "Текущее количество точек ${secondFigure.size}")

                val userChoice: String = readln()

                when (userChoice){
                    "1" -> secondFigure += coordinatesInput()
                    else -> {
                        if (secondFigure.size < 3){
                            println("Мало точек для создания многоугольника")
                            continue
                        }
                        break
                    }
                }
            }

            val firstFigureCalculations = calculateSquareAndPerimeter(*firstFigure)

            println("Площадь первой фигуры: ${firstFigureCalculations.first} \n" +
                    "Периметр первой фигуры: ${firstFigureCalculations.second}")

            val secondFigureCalculations = calculateSquareAndPerimeter(*secondFigure)

            println("Площадь второй фигуры: ${secondFigureCalculations.first} \n" +
                    "Периметр второй фигуры: ${secondFigureCalculations.second}")

            val intersectionResult = isIntersect(firstFigure, secondFigure)

            if (!intersectionResult.first){
                println("Фигуры не пересекаются")
                return
            }

            println("Фигуры пересекаются, площадь области пересечения: ${intersectionResult.second}")
            break
        }
        catch (e: Exception){
            println("Произошла ошибка")
            println(e.message)
            println(e.stackTrace.forEach { println(it) })
            println("Повторите попытку")
        }
    }

}

fun coordinatesInput(): Pair<Double, Double> {

    val x: Double
    val y: Double

    while (true){
        println("Введите координату X")
        val input = readln().toDoubleOrNull()
        if (input == null){
            println("Ввод не является числом")
            continue
        }
        x = input
        break
    }

    while (true){
        println("Введите координату Y")
        val input = readln().toDoubleOrNull()
        if (input == null){
            println("Ввод не является числом")
            continue
        }
        y = input
        break
    }
    return Pair(x, y)
}

fun calculateSquareAndPerimeter(vararg points: Pair<Double, Double>) : Pair<Double, Double>{

    var perimeter = .0
    var square = .0

    for (pointNum in points.indices){

        perimeter += calculatePerimeterWithCondition(points[pointNum % points.size],
            points[(pointNum+1) % points.size])

        square += points[pointNum % points.size].first * points[(pointNum+1) % points.size].second -
                points[pointNum % points.size].second * points[(pointNum+1) % points.size].first
    }

    return Pair(0.5 * abs(square), perimeter)
}

fun calculatePerimeterWithCondition(firstPoint: Pair<Double, Double>,
                                    secondPoint: Pair<Double, Double>): Double{

    if (firstPoint.first == secondPoint.first){
        return abs(secondPoint.second - firstPoint.second)
    }
    else if (firstPoint.second == secondPoint.second){
        return abs(secondPoint.first - firstPoint.first)
    }
    else{
        val firstCatheter: Double = abs(firstPoint.first - secondPoint.first)
        val secondCatheter: Double = abs(firstPoint.second - secondPoint.second)
        return sqrt(firstCatheter.pow(2) + secondCatheter.pow(2))
    }
}

fun areIntersecting(p1: Pair<Double,Double>, p2: Pair<Double,Double>, q1: Pair<Double,Double>, q2: Pair<Double,Double>)
    : Pair<Double,Double>? {
    val a1 = p2.second - p1.second
    val b1 = p1.first - p2.first
    val c1 = a1 * p1.first + b1 * p1.second

    val a2 = q2.second - q1.second
    val b2 = q1.first - q2.first
    val c2 = a2 * q1.first + b2 * q1.second

    val det = a1 * b2 - a2 * b1

    if (det == 0.0) {
        return null
    } else {
        val x = (b2 * c1 - b1 * c2) / det
        val y = (a1 * c2 - a2 * c1) / det

        val onSegment1 = minOf(p1.first, p2.first) <= x && x <= maxOf(p1.first, p2.first) &&
                minOf(p1.second, p2.second) <= y && y <= maxOf(p1.second, p2.second)
        val onSegment2 = minOf(q1.first, q2.first) <= x && x <= maxOf(q1.first, q2.first) &&
                minOf(q1.second, q2.second) <= y && y <= maxOf(q1.second, q2.second)

        return if (onSegment1 && onSegment2) {

            Pair(if (x == -0.0) abs(x) else (x),
                if (y == -0.0) abs(y) else (y))

        } else {
            null
        }
    }
}

fun isInside(point: Pair<Double,Double>, edgeStart: Pair<Double,Double>, edgeEnd: Pair<Double,Double>): Boolean {
    return (edgeEnd.first - edgeStart.first) * (point.second - edgeStart.second) -
            (edgeEnd.second - edgeStart.second) * (point.first - edgeStart.first) >= 0
}

fun isClockwise(polygon: Array<Pair<Double, Double>>): Boolean {
    var sum = 0.0
    for (i in polygon.indices) {
        val (x1, y1) = polygon[i]
        val (x2, y2) = polygon[(i + 1) % polygon.size]
        sum += (x2 - x1) * (y2 + y1)
    }
    return sum > 0
}

fun isIntersect(subjectPolygon: Array<Pair<Double, Double>>, clipPolygon: Array<Pair<Double, Double>>): Pair<Boolean, Double?> {

    if (isClockwise(subjectPolygon)) {
        subjectPolygon.reverse()
    }
    if (isClockwise(clipPolygon)) {
        clipPolygon.reverse()
    }

    var clipped = subjectPolygon.toList()

    for (edgeIndex in clipPolygon.indices) {
        val inputList: MutableList<Pair<Double,Double>> = mutableListOf()
        inputList.addAll(clipped)
        clipped = mutableListOf()

        val clipEdgeStart = clipPolygon[edgeIndex]
        val clipEdgeEnd = clipPolygon[(edgeIndex + 1) % clipPolygon.size]

        for (i in inputList.indices) {
            val currentPoint = inputList[i]
            val prevPoint = inputList[(i - 1 + inputList.size) % inputList.size]

            val currentInside = isInside(currentPoint, clipEdgeStart, clipEdgeEnd)
            val prevInside = isInside(prevPoint, clipEdgeStart, clipEdgeEnd)

            if (currentInside) {
                if (!prevInside) {
                    areIntersecting(prevPoint, currentPoint, clipEdgeStart, clipEdgeEnd)?.let { clipped.add(it) }
                }
                clipped.add(currentPoint)
            } else if (prevInside) {
                areIntersecting(prevPoint, currentPoint, clipEdgeStart, clipEdgeEnd)?.let { clipped.add(it) }
            }
        }
    }

    if (clipped.isEmpty()) return Pair(false, null)

    val area = calculateSquareAndPerimeter(*clipped.toTypedArray()).first

    return if (area != 0.0) Pair(true, area) else Pair(false, null)
}