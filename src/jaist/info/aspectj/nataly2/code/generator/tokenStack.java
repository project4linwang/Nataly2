package jaist.info.aspectj.nataly2.code.generator;

import java.util.Stack;

public class tokenStack {
	public static boolean check(String input){
		   Stack<Character> stack=new Stack<Character>();
		   for(int i=0;i<input.length();i++){
			   char ch=input.charAt(i);
			   switch(ch){
			       case '(':
			    	   stack.push(ch);
				       break;
			       case ')':
			    	   if(!stack.isEmpty()){
			    		   char chx=stack.pop();
			    		   if(ch==')' && chx!='('){
			    			   return false;
			    		   }
			    	   }
			    	   else{
			    		   return false;
			    	   }
			    	   break;
			    	 default:
			    		 break;
			   }
		   }
		   if(!stack.isEmpty()){
			   return false;
		   }
		   return true;
	   }
}
