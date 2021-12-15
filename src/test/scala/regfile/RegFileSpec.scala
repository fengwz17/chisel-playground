package regfile

import chisel3._
import chiseltest._
import chiseltest.formal._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class Checker extends Module {
  val io = IO(new Bundle {
    val wb    = Input(new WriteBack)
    val state = Input(new State)
  })

  val regFile1 = Module(new RegFile)
  val regFile2 = Module(new RegFile)

  regFile1.io.wb := io.wb
  regFile2.io.wb := io.wb

  for (i <- 0 until 32) {
    assert(regFile1.io.state.reg(i.U) === regFile2.io.state.reg(i.U))
  }
}

class RegFileSpec extends AnyFlatSpec with Formal with ChiselScalatestTester {
  behavior of "RegFile"
  it should "pass" in {
    verify(new Checker, Seq(BoundedCheck(5)))
  }

  behavior of "RegFileChecker"
  it should "pass" in {
    class TestModule extends RegFile {
      val checker = Module(new RegFileChecker)
      checker.io.wb        := io.wb
      checker.io.state.reg := reg
    }
    verify(new TestModule, Seq(BoundedCheck(5)))
  }
}
