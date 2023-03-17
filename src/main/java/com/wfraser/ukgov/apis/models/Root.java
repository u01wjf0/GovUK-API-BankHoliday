package com.wfraser.ukgov.apis.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wfraser.ukgov.apis.constants.Regions;

/**
 * 
 * @author u01wjf0
 *
 */
public class Root{
	@JsonProperty("england-and-wales")
	public EnglandAndWales ew;
	@JsonProperty("scotland")
	public Scotland scotland;
	@JsonProperty("northern-ireland")
	public NorthernIreland ni;

	public Events getAllEvents(Regions[] regions) {


		Events events = new Events();
		if(regions == null || regions.length == 0)
		{
			regions = new Regions[] {Regions.ALL};
		}

		for(Regions r : regions)
		{
			if(r == Regions.ALL) {
				regions = new Regions[] {Regions.ALL};
				break;
			}
		}

		for(Regions r : regions) {
			switch (r) {
			case SCOTLAND :
				events.addAll(scotland.events);
				break;
			case ENGLAND_AND_WALES :
				events.addAll(ew.events);
				break;
			case NORTHERN_IRELAND :
				events.addAll(ni.events);
				break;
			default:
				events.addAll(scotland.events);
				events.addAll(ew.events);
				events.addAll(ni.events);
				break;
			}
		}

		return events;
	}
}
