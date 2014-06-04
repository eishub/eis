import org.junit.Test;
import static org.junit.Assert.*;

public class ILLConventionsTest {

	@Test
	public void testCorrectExamples() {
		
		try {
			IILExamples.correctExamples();
		}
		catch (AssertionError e) {
			fail("convention failure in correct examples");
		}
		
	}
	
	@Test
	public void testWrongExamples() {
		
		try {
			IILExamples.wrongExamples();
			fail("convention failure in wrong examples");
		}
		catch (AssertionError e) {
		}
		
	}

}
