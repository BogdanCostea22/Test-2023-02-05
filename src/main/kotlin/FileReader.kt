import java.io.File

data class GridDimension(
    val width: Int,
    val height: Int
)

data class Point(
    val x: Int,
    val y: Int
)

data class TestCase(
    val gridDimension: GridDimension,
    val startingPoint: Point,
    val endPoint: Point,
    val obstacles: List<Obstacle>
)

fun MutableList<String>.getIntAndRemove(index: Int = 0): Int {
    return this.removeAt(index).toInt()
}

typealias Obstacle = Pair<Point, Point>

fun <T> MutableList<String>.removeLineAndExecute(action: (String) -> T): T {
    val line = this.removeAt(0)
    return action(line)
}

object FileReaderImpl : FileReader {
    private fun getStartAndEndPoint(line: String): Pair<Point, Point> {
        val coordinates = line.split(" ").map(String::toInt)

        return Point(x = coordinates[0], y = coordinates[1]) to Point(x = coordinates[2], y = coordinates[3])
    }

    private fun createTestCases(numberOfTestCases: Int, fileContent: MutableList<String>): List<TestCase> {
        val testCases = mutableListOf<TestCase>()

        (0 until numberOfTestCases).forEach { _ ->
            //Parse data grid dimension
            val gridDimension = fileContent.removeLineAndExecute { coordinatesLine ->
                val coordinates = coordinatesLine.split(" ")
                GridDimension(coordinates.first().toInt(), coordinates[1].toInt())
            }

            //Get the starting and ending point
            val (startPoint, endPoint) = fileContent.removeLineAndExecute { startAndEndCoordinates ->
                getStartAndEndPoint(startAndEndCoordinates)
            }

            //Parse the obstacles
            val numberOfObstacles = fileContent.getIntAndRemove()
            val obstacles: List<Obstacle> = (0 until numberOfObstacles).map {
                fileContent.removeLineAndExecute(this::getStartAndEndPoint)
            }

            testCases.add(
                TestCase(gridDimension, startPoint, endPoint, obstacles)
            )
        }

        return testCases
    }

    override fun readFile(fileName: String): List<TestCase> {
        val resourceUrl = this::class.java.getResource(fileName).toURI()
        val lines = File(resourceUrl).readLines().toMutableList()

        //Get the numbers of testCases
        val numberOfTestCases = lines.getIntAndRemove()
        return createTestCases(numberOfTestCases, lines)
    }
}


interface FileReader {
    fun readFile(fileName: String): List<TestCase>
}

