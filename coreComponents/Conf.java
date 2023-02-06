package coreComponents;

/* To iterate over all configurations for evaluation you might want to use
 * for(Conf conf:Conf.values()) {
 *  //do something with conf
 *}
 */

public enum Conf{


	//************************TEST Configurations  

	JCONF00(Map.JMAP00,1,1,3,4), //JCONF00 is the configuration in the spec
	JCONF11(Map.JMAP00,1,1,2,3), //JCONF11 fails

	//configurations used in the tests 
	JCONF01(Map.JMAP01,1,1,1,2),
	JCONF02(Map.JMAP01,2,1,2,2),
	JCONF03(Map.JMAP01,0,0,2,2), 
	JCONF04(Map.JMAP02,0,2,0,0),   
	JCONF05(Map.JMAP02,0,0,2,2),   



	//************************Configurations for evaluation 

	CONF0(Map.MAP0,0,0,5,5),
	CONF1(Map.MAP0,5,5,0,0),
	CONF2(Map.MAP0,0,5,5,0),
	CONF3(Map.MAP0,5,2,1,4),
	CONF4(Map.MAP0,4,4,0,2),

	CONF5(Map.MAP1,0,0,4,4),
	CONF6(Map.MAP1,4,4,0,0),
	CONF7(Map.MAP1,2,0,2,4),
	CONF8(Map.MAP1,4,0,0,0),
	CONF9(Map.MAP1,4,3,1,1),

	CONF10(Map.MAP2,5,5,2,3),
	CONF11(Map.MAP2,2,3,1,4),
	CONF12(Map.MAP2,5,0,4,5),
	CONF13(Map.MAP2,4,1,0,5),
	CONF14(Map.MAP2,0,0,4,5),

	CONF15(Map.MAP3,0,0,7,7),
	CONF16(Map.MAP3,9,9,7,8),
	CONF17(Map.MAP3,4,0,4,9),
	CONF18(Map.MAP3,1,1,4,5),
	CONF19(Map.MAP3,3,7,4,2),

	CONF20(Map.MAP4,0,7,6,1),
	CONF21(Map.MAP4,6,5,0,0),
	CONF22(Map.MAP4,1,0,4,3),
	CONF23(Map.MAP4,6,0,2,5),
	CONF24(Map.MAP4,0,1,6,6)
	;

	private final Map map; 
	private final Coord s;
	private final Coord g;


	Conf(Map map, int rs, int cs, int rg, int cg){
		this.map=map;
		s=new Coord(rs,cs);//start: departure port
		g=new Coord(rg,cg);//goal: destination port 
	}

	public Map getMap() {
		return map;
	}

	public Coord getS() {
		return s;
	}

	public Coord getG() {
		return g;
	}

}
