package com.nagarro.custom.authorizer;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Scanner;

public class App {

	public static void main(String args[]) {
//		String[] str = "sahilmehta".split(":");
////		System.out.println(str.length);
//		for(String val : str) {
////			System.out.println(val);
//		}
//		
//		Date a = new Date(new Long("1583223495345"));
//		String ass = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.\r\n" + 
//				"eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.\r\n" + 
//				"TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ";
//		System.out.println(ass.getBytes());

		// create 2 BigDecimal Objects
		Scanner scn = new Scanner(System.in);
		String n = scn.next();

		while (!n.equalsIgnoreCase("n")) {

			BigDecimal bg1 = new BigDecimal(n);
			String str = "The value " + bg1 + " after rounding is "
					+ bg1.setScale(0, RoundingMode.HALF_DOWN);

			// print bg2 value
			System.out.println(str);
			n = scn.next();
		}

	}

}
