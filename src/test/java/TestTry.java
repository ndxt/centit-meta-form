import org.junit.Test;

public class TestTry{
	
	@Test
	public void test(){
		System.out.println(getAString());
	}
	
	
	public String getAString(){
		try{
			int a=0;
			int b=4/a;
			return "333";
		}catch(Exception e){
			return "222";
		}
		finally{
			///return "111";
		}
	}
}