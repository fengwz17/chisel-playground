package Inverter

import chisel3._
import chisel3.stage._
import firrtl.options.Dependency
import firrtl.stage.RunFirrtlTransformAnnotation
import firrtl.backends.experimental.smt.random._
import firrtl.backends.experimental.smt._

class Inverter extends Module {
 val in = IO(Input(Bool()))
 val out = IO(Output(Bool()))
 val hold = IO(Input(Bool()))

 val delay = Reg(Bool())
 when(!hold) {
   delay := !in
 }
 out := delay
}

object InverterPrint extends App {
    val compiler = new ChiselStage
    val r = compiler.execute(Array("-E", "experimental-btor2"),
    Seq(ChiselGeneratorAnnotation(() => new Inverter()),
        RunFirrtlTransformAnnotation(Dependency(InvalidToRandomPass)),
        
        ))
    println("Transiton System")
    println(r.collectFirst { case TransitionSystemAnnotation(s) => s }.get.serialize)
    println()
    println("Btor2:")
    println(r.collectFirst { case EmittedSMTModelAnnotation(_, src, _) => src }.get)
}