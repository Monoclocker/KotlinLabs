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

        perimeter += calculatePerimeterWithCondition(points[pointNum % (points.size-1)],
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
            Pair(x, y)
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
    : Pair<Boolean, Double?>{

    val firstClone = first.clone()
    val secondClone = second.clone()

    var indicator = .0

    for (pointNum in first.indices){
        indicator += (first[(pointNum+1) % first.size].first - first[pointNum % first.size].first) *
                (first[(pointNum+1) % first.size].second + first[pointNum % first.size].second)
    }

    if(indicator < 0){
        firstClone.reverse()
    }

    indicator = .0

    for (pointNum in second.indices){
        indicator += (second[(pointNum+1) % second.size].first - second[pointNum % second.size].first) *
                (second[(pointNum+1) % second.size].second + second[pointNum % second.size].second)
    }

    if (indicator < 0){
        secondClone.reverse()
    }

//    if (first.contentEquals(second)){
//        return Pair(true, calculateSquareAndPerimeter(*first).first)
//    }

    var intersectionMeansExit: Boolean = isPointInsidePolygon(secondClone.first(), firstClone)

    var firstPolygonExtendedArray: Array<Triple<Double, Double, Boolean?>> = emptyArray()
    var secondPolygonExtendedArray: Array<Triple<Double, Double, Boolean?>> = emptyArray()

    val firstSize = firstClone.size
    val secondSize = secondClone.size

    for(secondPointNum in secondClone.indices){

        secondPolygonExtendedArray +=
            Triple(secondClone[secondPointNum].first, secondClone[secondPointNum].second, null)

        for (firstPointNum in firstClone.indices){

            val intersectionPoint = areIntersecting(secondClone[secondPointNum % secondSize],
                secondClone[(secondPointNum + 1) %  secondSize], firstClone[firstPointNum % firstSize],
                firstClone[(firstPointNum + 1) % firstSize])

            if (intersectionPoint == null){
                continue
            }

            secondPolygonExtendedArray += Triple(intersectionPoint.first,
                intersectionPoint.second, intersectionMeansExit)
            intersectionMeansExit = !intersectionMeansExit
        }
    }

    for(firstPointNum in firstClone.indices){

        firstPolygonExtendedArray +=
            Triple(firstClone[firstPointNum].first, firstClone[firstPointNum].second, null)

        for (secondPointNum in secondClone.indices){

            val intersectionPoint = areIntersecting(secondClone[secondPointNum % secondSize],
                secondClone[(secondPointNum + 1) %  secondSize], firstClone[firstPointNum % firstSize],
                firstClone[(firstPointNum + 1) % firstSize])

            if (intersectionPoint == null){
                continue
            }

            firstPolygonExtendedArray += secondPolygonExtendedArray.first {
                it.first == intersectionPoint.first && it.second == intersectionPoint.second
            }
        }
    }

    val point = secondPolygonExtendedArray.first{ it.third == false }

    var intersectionPoints: Array<Pair<Double,Double>> = arrayOf(Pair(point.first, point.second))

    var pointIndex = (secondPolygonExtendedArray.indexOf(point) + 1) % secondPolygonExtendedArray.size
    var currentPoint = secondPolygonExtendedArray[pointIndex]

    var observablePoligon = secondPolygonExtendedArray.clone()
    var isFirstObserved = false

    while (currentPoint != point){

        intersectionPoints += Pair(currentPoint.first, currentPoint.second)

        if (currentPoint.third != null){
            observablePoligon = when (isFirstObserved) {
                true -> secondPolygonExtendedArray.clone()
                false -> firstPolygonExtendedArray.clone()
            }
            isFirstObserved = !isFirstObserved
        }

        currentPoint = observablePoligon.first { it == currentPoint }
        pointIndex = (observablePoligon.indexOf(currentPoint) + 1) % observablePoligon.size
        currentPoint = observablePoligon[pointIndex]
    }

    if (intersectionPoints.isEmpty()){
        return Pair(false, null)
    }

    return Pair(true, calculateSquareAndPerimeter(*intersectionPoints).first)
}