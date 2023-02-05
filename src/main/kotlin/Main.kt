import java.util.*

enum class Speed(val x: Int, val y: Int) {
    UP_LEFT(-1, -1), UP_MIDDLE(-1, 0), UP_RIGHT(-1, 1),
    LEFT(0, -1), SAME(0, 0), RIGHT(0, 1),
    DOWN_LEFT(1, -1), DOWN_MIDDLE(1, 0), DOWN_RIGHT(1, 1)
}


data class PointWithSpeed(
    val point: Point,
    var velocityx: Int,
    var velocityY: Int
)


fun List<Obstacle>.isEmptyAt(x: Int, y: Int): Boolean =
    none { (startPoint, endPoint) -> x in startPoint.x..endPoint.x && y in startPoint.y..endPoint.y }

fun Int.isVelocityInRange(): Boolean =
    this in -3..3

fun Pair<Int, Int>.areInGrid(gridDimension: GridDimension): Boolean =
    first in 0..gridDimension.width && second in 0..gridDimension.height

fun bfs(startPoint: Point, endPoint: Point, gridDimension: GridDimension, obstacles: List<Obstacle>): Int {
    val queue = LinkedList<PointWithSpeed>()
    queue.add(PointWithSpeed(startPoint, 0, 0))

    val visited = mutableSetOf<Point>()
    visited.add(startPoint)

    var hops = 0

    while (queue.isNotEmpty()) {
        queue.indices.forEach { _ ->
            val curr = queue.poll()

            if (curr.point == endPoint) {
                return hops
            }

            Speed.values().forEach { speed ->
                val x = curr.point.x + curr.velocityx + speed.x
                val y = curr.point.y + curr.velocityY + speed.y
                val nextVelocityX = curr.velocityx + speed.x
                val nextVelocityY = curr.velocityY + speed.y

                if ((x to y).areInGrid(gridDimension) && obstacles.isEmptyAt(x, y) && nextVelocityX.isVelocityInRange() && nextVelocityY.isVelocityInRange()
                ) {
                    val nextPoint = Point(x, y)
                    val next = PointWithSpeed(Point(x, y), nextVelocityX, nextVelocityY)
                    if (!visited.contains(nextPoint)) {
                        queue.add(next)
                        visited.add(nextPoint)
                    }
                }
            }
        }

        hops++
    }

    return -1
}


fun main() {
    FileReaderImpl.readFile("input.txt").forEach { (gridDimension, startPoint, endPoint, obstacles) ->
      println(bfs(startPoint, endPoint, gridDimension, obstacles))
    }
}