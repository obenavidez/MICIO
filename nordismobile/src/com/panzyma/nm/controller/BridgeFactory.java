package com.panzyma.nm.controller;

import com.panzyma.nm.CBridgeM.BBaseM;
import com.panzyma.nm.CBridgeM.BClienteM;
import com.panzyma.nm.CBridgeM.BConfiguracionM;
import com.panzyma.nm.CBridgeM.BDevolucionM;
import com.panzyma.nm.CBridgeM.BLogicM;
import com.panzyma.nm.CBridgeM.BPedidoM;
import com.panzyma.nm.CBridgeM.BProductoM;
import com.panzyma.nm.CBridgeM.BReciboM;
import com.panzyma.nm.CBridgeM.BSolicitudDescuentoM;
import com.panzyma.nm.CBridgeM.BVentaM;

public final class BridgeFactory {
	
	private static BBaseM bridge;

	public enum BridgeType {
		BCLIENTEM("BClienteM"),
		BPEDIDOM("BPedidoM"),
		BRECIBOM("BReciboM"),
		BCONFIGURACIONM("BConfiguracionM"),
		BLOGICM("BLogicM"),
		BPRODUCTOM("BProductoM"),
		BVENTAM("BVentaM"),
		BSOLICITUDDESCUENTOM("BSolicitudDescuentoM"),
		BDEVOLUCIONM("BDevolucionM");

		private String name;

		private BridgeType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static BridgeType getType(String _name) {
			BridgeType type = BCLIENTEM;
			for (BridgeType bt : values()) {
				if (bt.name.equals(_name)) {
					type = bt;
					break;
				}
			}
			return type;
		}
	}

	private BridgeFactory() {}

	public static BBaseM getBridge(String value) {
		BridgeType type = BridgeType.getType(value);
		switch (type) {
		case BPEDIDOM:
			bridge = new BPedidoM();
			break;
		case BRECIBOM:
			bridge = new BReciboM();
			break;
		case BCLIENTEM:
			bridge = new BClienteM();
			break;
		case BPRODUCTOM:
			bridge = new BProductoM();
			break;
		case BLOGICM:
			bridge = new BLogicM();
			break;
		case BVENTAM:
			bridge = new BVentaM();
			break;
		case BCONFIGURACIONM:
			bridge = new BConfiguracionM();
			break;
		case BSOLICITUDDESCUENTOM:
			bridge = new BSolicitudDescuentoM();
			break;
		case BDEVOLUCIONM:
			bridge = new BDevolucionM();
			break;
		default:
			break;
		}
		return bridge;
	}

}
