import kotlin.math.abs

fun main() {

    data class Sensor(
        val x: Int,
        val y: Int,
        val distanceScanned: Int
    )

    data class Beacon(
        val x: Int,
        val y: Int
    )

    val sensors = mutableListOf<Sensor>()
    val beacons = mutableListOf<Beacon>()
    readInput("Day15")
        .map {
            val match =
                Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)").find(it)!!
            val (xSensor, ySensor, xBeacon, yBeacon) = match.destructured
            beacons.add(Beacon(x = xBeacon.toInt(), y = yBeacon.toInt()))
            sensors.add(
                Sensor(
                    x = xSensor.toInt(),
                    y = ySensor.toInt(),
                    distanceScanned = abs(xBeacon.toInt() - xSensor.toInt()) + abs(yBeacon.toInt() - ySensor.toInt())
                )
            )
        }

    val uniqueBeacons = beacons.distinct()

    fun getNonBeaconRanges(y: Int): List<IntRange> {
        val result = sensors
            .mapNotNull {
                val remain = it.distanceScanned - abs(it.y - y)
                if (remain > 0) {
                    it.x - remain..it.x + remain
                } else {
                    null
                }
            }
            .sortedBy { it.first }

        val filtered = mutableListOf<IntRange>()
        result.forEach { current ->
            var added = false
            filtered.forEach inside@{
                if (it.contains(current.first) && it.contains(current.last)) {
                    added = true
                    return@inside
                }

                if (it.contains(current.first)) {
                    filtered.remove(it)
                    filtered.add(it.first..current.last)
                    added = true
                    return@inside
                }

                if (it.contains(current.last)) {
                    filtered.remove(it)
                    filtered.add(current.first..it.last)
                    added = true
                    return@inside
                }
            }

            if (!added) {
                filtered.add(current)
            }
        }

        return filtered
    }


    fun part1(): Int {
        val y = 2000000

        return getNonBeaconRanges(2_000_000).let { nonRanges ->
            nonRanges.sumOf { it.last - it.first + 1 } -
                    uniqueBeacons.count { beacon -> beacon.y == y && nonRanges.any { it.contains(beacon.x) } }
        }

    }

    fun part2(): Long {
        val y = (0..4_000_000).first {
            getNonBeaconRanges(it).size >= 2
        }
        val x = getNonBeaconRanges(y).first().last + 1
        return x.toLong() * 4_000_000L + y.toLong()
    }

    println(part1())
    println(part2())
}