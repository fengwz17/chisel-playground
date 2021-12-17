package regfile

import chisel3._

class WriteIO extends Bundle {
  val valid = Input(Bool())
  val dest  = Input(UInt(2.W))
  val data  = Input(UInt(64.W))
}

class ReadIO extends Bundle {
  val idx  = Input(UInt(2.W))
  val data = Output(UInt(64.W))
}

class RegFile extends Module {
  val io = IO(new Bundle {
    val wt = new WriteIO
    val rd = new ReadIO
  })

  val reg = RegInit(VecInit(Seq.fill(4)(0.U(64.W))))

  when(io.wt.valid) {
    reg(io.wt.dest) := io.wt.data
  }

  io.rd.data := reg(io.rd.idx)
}
