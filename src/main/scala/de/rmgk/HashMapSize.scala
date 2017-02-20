package de.rmgk

import java.util
import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(3)
class HashMapSize {
	@Param(Array(/*"1", "10", "100", "1000",*/ "10000", "100000"))
	var size: Int = _

	lazy val keys = Range(0, size).map(_.toString)

	@Benchmark
	def insertWithoutSizeHint(): Boolean = {
		val hm = new util.HashMap[String, Boolean]()
		keys.foreach(k => hm.put(k, true))
		hm.get("0")
	}

	@Benchmark
	def insertWithSizeHint(): Boolean = {
		val hm = new util.HashMap[String, Boolean](2 * size)
		keys.foreach(k => hm.put(k, true))
		hm.get("0")
	}
}
