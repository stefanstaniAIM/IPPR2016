package at.fhjoanneum.ippr.processengine;

import org.springframework.stereotype.Service;

@Service
public class BusinessService {

	public void perform(final Object o) {
		System.out.println("Perform: " + o);
	}
}
