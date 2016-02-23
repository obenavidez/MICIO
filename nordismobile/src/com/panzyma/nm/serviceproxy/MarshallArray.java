package com.panzyma.nm.serviceproxy;

import java.io.IOException;

import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;

public class MarshallArray implements Marshal 
{
    
	@Override
	public Object readInstance(XmlPullParser parser, String namespace, String name,
			PropertyInfo info) throws IOException, XmlPullParserException {
		// TODO Auto-generated method stub
		return parser.nextText();
	}

	@Override
	public void register(SoapSerializationEnvelope sse) {
		// TODO Auto-generated method stub
		sse.addMapping(sse.xsd, "PromocionesAplicadas[]", PedidoPromocion[].class,this);
	}

	@Override
	public void writeInstance(XmlSerializer writer, Object obj)
			throws IOException {
		// TODO Auto-generated method stub
		PedidoPromocion[] pp=(PedidoPromocion[]) obj;
		Log.d("", pp[0].toString());
	}

}
