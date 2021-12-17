package regfile

import chisel3._
import chiseltest._
import chiseltest.formal._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class CheckerA extends Module {
  val io = IO(new Bundle {
    val wt = new WriteIO
    val rd = new ReadIO
  })

  val regFile1 = Module(new RegFile)
  val regFile2 = Module(new RegFile)

  regFile1.io.wt := io.wt
  regFile2.io.wt := io.wt

  regFile1.io.rd.idx := io.rd.idx
  regFile2.io.rd.idx := io.rd.idx

  // here is a bug on purpos，
  assert(regFile1.io.rd.data =/= regFile2.io.rd.data)
  // assert(regFile1.io.rd.data === regFile2.io.rd.data)

  io.rd.data := regFile1.io.rd.data
}

class CheckerB extends Module {
  val io = IO(new Bundle {
    val wt = new WriteIO
    val rd = new ReadIO
  })

  val reg1 = RegInit(VecInit(Seq.fill(4)(0.U(64.W))))
  val reg2 = RegInit(VecInit(Seq.fill(4)(0.U(64.W))))

  when(io.wt.valid) {
    reg1(io.wt.dest) := io.wt.data
    reg2(io.wt.dest) := io.wt.data
  }

  // here is a bug on purpos，
  assert(reg1(io.rd.idx) =/= reg2(io.rd.idx))
  // assert(reg1(io.rd.idx) === reg2(io.rd.idx))

  io.rd.data := reg1(io.rd.idx)
}

class RegFileSpec extends AnyFlatSpec with Formal with ChiselScalatestTester {
  behavior of "CheckerA"
  it should "pass" in {
    verify(new CheckerA, Seq(BoundedCheck(5)))
  }
  behavior of "CheckerB"
  it should "pass" in {
    verify(new CheckerB, Seq(BoundedCheck(5)))
  }
}
