import static org.junit.Assert.fail;

import org.junit.Test;

public class ILLConventionsTest {
	@Test
	public void testCorrectExamples() {
		try {
			IILExamples.correctExamples();
		} catch (final AssertionError e) {
			fail("convention failure in correct examples");
		}
	}

	@Test
	public void testWrongExamples() {
		try {
			IILExamples.wrongExamples();
			fail("convention failure in wrong examples");
		} catch (final AssertionError e) {
		}
	}
}
