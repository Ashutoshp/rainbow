module rubis.strategies;

import op "org.sa.rainbow.stitch.RubisTest";
import op "org.sa.rainbow.stitch.StitchTest";
import op "org.sa.rainbow.stitch.lib.*";

define int numberOfServers = Set.size({"check"});

tactic TTrueTactic(string name, int foo) {
  condition {
    true;
  }
  action {
    StitchTest.markExecuted("TTrueTactic");
  }
  effect {
    true;
  }
}

strategy s [numberOfServers > 49] {
	t1: (true) -> TTrueTactic(2, 2.1) {
		ta1: (true) -> done;
	}
}

