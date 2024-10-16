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

fun isPointInsidePolygon(point: Pair<Double, Double>, polygon: Array<Pair<Double, Double>>): Boolean {
    var intersections = 0
    val size = polygon.size
    for (i in polygon.indices) {
        val p1 = polygon[i]
        val p2 = polygon[(i + 1) % size]

        if ((point.second > minOf(p1.second, p2.second)) && (point.second <= maxOf(p1.second, p2.second)) &&
            (point.first <= maxOf(p1.first, p2.first))) {
            val xIntersections = (point.second - p1.second) * (p2.first - p1.first) / (p2.second - p1.second) + p1.first
            if (p1.first == p2.first || point.first <= xIntersections) {
                intersections++
            }
        }
    }
    return intersections % 2 != 0
}

fun isIntersect(first: Array<Pair<Double,Double>>, second: Array<Pair<Double, Double>>)
        : Pair<Boolean, Double?> {

    if (second.all { isPointInsidePolygon(it, first) }){
        return Pair(true, calculateSquareAndPerimeter(*second).first)
    }

    if (first.all { isPointInsidePolygon(it, second) }){
        return Pair(true, calculateSquareAndPerimeter(*first).first)
    }

    if (first.contentEquals(second)){
        return Pair(true, calculateSquareAndPerimeter(*first).first)
    }

    val firstClone = first.clone()
    val secondClone = second.clone()

    if (!isClockwise(firstClone)) {
        firstClone.reverse()
    }
    if (!isClockwise(secondClone)) {
        secondClone.reverse()
    }
    val intersectionPoints = mutableListOf<Pair<Double, Double>>()

    for (i in firstClone.indices) {
        for (j in secondClone.indices) {
            val intersection = areIntersecting(
                firstClone[i], firstClone[(i + 1) % firstClone.size],
                secondClone[j], secondClone[(j + 1) % secondClone.size]
            )
            if (intersection != null) {
                intersectionPoints.add(intersection)
            }
        }
    }

    for (point in firstClone) {
        if (isPointInsidePolygon(point, secondClone)) {
            intersectionPoints.add(point)
        }
    }

    for (point in secondClone) {
        if (isPointInsidePolygon(point, firstClone)) {
            intersectionPoints.add(point)
        }
    }

    if (intersectionPoints.isEmpty()) {
        return Pair(false, null)
    }

    intersectionPoints.sortWith(compareBy({ it.first }, { it.second }))

    val area = calculateSquareAndPerimeter(*(intersectionPoints.toTypedArray()))

    return Pair(true, area.first)
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
