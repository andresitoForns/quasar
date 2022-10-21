package com.andres.quasar.logic

import com.lemmingapex.trilateration.TrilaterationFunction
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresFactory
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.DiagonalMatrix

class TrilaterationSolver(
    private val function: TrilaterationFunction, private val leastSquaresOptimizer: LeastSquaresOptimizer
) {
    private val MAX_NUMBER_OF_ITERATIONS = 1000
    private fun solveIt(
        target: DoubleArray?, weights: DoubleArray?, initialPoint: DoubleArray?
    ): Optimum {
        val leastSquaresProblem = LeastSquaresFactory.create( // function to be optimized
            function,  // target values at optimal point in least square equation
            // (x0+xi)^2 + (y0+yi)^2 + ri^2 = target[i]
            ArrayRealVector(target, false),
            ArrayRealVector(initialPoint, false),
            DiagonalMatrix(weights),
            null,
            MAX_NUMBER_OF_ITERATIONS,
            MAX_NUMBER_OF_ITERATIONS
        )
        return leastSquaresOptimizer.optimize(leastSquaresProblem)
    }

    fun solve(): Optimum {
        val numberOfPositions = function.positions.size
        val positionDimension = function.positions[0].size
        val initialPoint = DoubleArray(positionDimension)
        // initial point, use average of the vertices
        for (i in function.positions.indices) {
            val vertex = function.positions[i]
            for (j in vertex.indices) {
                initialPoint[j] += vertex[j]
            }
        }
        for (j in initialPoint.indices) {
            initialPoint[j] /= numberOfPositions.toDouble()
        }
        val target = DoubleArray(numberOfPositions)
        val distances = function.distances
        val weights = DoubleArray(target.size)
        for (i in target.indices) {
            target[i] = 0.0
            weights[i] = inverseSquareLaw(distances[i])
        }
        return solveIt(target, weights, initialPoint)
    }

    private fun inverseSquareLaw(distance: Double): Double {
        return 1 / (distance * distance)
    }

}