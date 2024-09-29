package lab1
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

fun main(){

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

    println("Площадь первой фигуры: ${secondFigureCalculations.first} \n" +
            "Периметр первой фигуры: ${secondFigureCalculations.second}")

    val intersectionResult = isIntersect(firstFigure, secondFigure)

    if (!intersectionResult.first){
        println("Фигуры не пересекаются")
        return
    }

    println("Фигуры пересекаются, площадь области пересечения: ${intersectionResult.second}")
}

fun coordinatesInput(): Pair<Double, Double> {

    println("Введите координату X")
    val x: Double = readln().toDouble()

    println("Введите координату Y")
    val y: Double = readln().toDouble()

    return Pair(x, y)
}

fun calculateSquareAndPerimeter(vararg points: Pair<Double, Double>) : Pair<Double, Double>{

    var perimeter = .0
    var square = .0

    for (pointNum in 0 ..< points.size-1){

        perimeter += calculatePerimeterWithCondition(points[pointNum], points[pointNum+1])

        square += points[pointNum].first * points[pointNum+1].second -
                points[pointNum].second * points[pointNum+1].first
    }

    perimeter += calculatePerimeterWithCondition(points[points.size-1], points[0])

    square += points[points.size-1].first * points[0].second -
            points[points.size-1].second * points[0].first

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
        val firstCatheter: Double = abs(firstPoint.first - firstPoint.first)
        val secondCatheter: Double = abs(secondPoint.second - secondPoint.second)
        return sqrt(firstCatheter.pow(2) + secondCatheter.pow(2))
    }
}



fun isIntersect(first: Array<Pair<Double,Double>>, second: Array<Pair<Double, Double>>) : Pair<Boolean, Double?>{

    //TODO: Реализовать функцию проверки пересечения
    return Pair(false, null)
}