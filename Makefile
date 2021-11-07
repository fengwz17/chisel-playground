BUILD_PATH = ./build

elaborate:
	sbt "runMain elaborate.Elaborate -td $(BUILD_PATH)"

counter-formal:
	sbt "runMain counter.CounterFormal"
	cd $(BUILD_PATH) ;\
	export filename=Counter ;\
	$$FIRRTL -i $$filename.hi.fir -E low ;\
	$$FIRRTL -i $$filename.hi.fir -E low -fil Counter -o $$filename.il.opt.lo.fir ;\
	$$FIRRTL -i $$filename.il.opt.lo.fir -E smt2
	cd $(BUILD_PATH) ;\
	export filename=CounterProp ;\
	$$FIRRTL -i $$filename.hi.fir -E low ;\
	$$FIRRTL -i $$filename.hi.fir -E low -fil Counter -o $$filename.il.opt.lo.fir ;\
	$$FIRRTL -i $$filename.il.opt.lo.fir -E smt2

clean:
	rm -rf $(BUILD_PATH)