package com.panzyma.nm.auxiliar;

import java.io.IOException;
import java.util.Vector;

import org.ksoap2.serialization.AttributeInfo;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

public class SMFSerializacionSoap extends SoapSerializationEnvelope {

	public static final String TAG = SMFSerializacionSoap.class.getSimpleName();
	Vector multiRef;
	static final Marshal DEFAULT_MARSHAL=null;
	
	public SMFSerializacionSoap(int version) {
		super(version);
	}

	@Override
	public Object[] getInfo(Object type, Object instance)
	  {
	    if (type == null) {
	      if (((instance instanceof SoapObject)) || ((instance instanceof SoapPrimitive))) {
	        type = instance;
	      } else {
	        type = instance.getClass();
	      }
	    }
	    if ((type instanceof SoapObject))
	    {
	      SoapObject so = (SoapObject)type;
	      return new Object[] { so.getNamespace(), so.getName(), null, null };
	    }
	    if ((type instanceof SoapPrimitive))
	    {
	      SoapPrimitive sp = (SoapPrimitive)type;
	      return new Object[] { sp.getNamespace(), sp.getName(), null, DEFAULT_MARSHAL };
	    }
	    if (((type instanceof Class)) && (type != PropertyInfo.OBJECT_CLASS))
	    {
	      Object[] tmp = (Object[])this.classToQName.get(((Class)type).getName());
	      if (tmp != null) {
	        return tmp;
	      }
	    }
	    return new Object[] { this.xsd, "anyType", null, null };
	  }
	
	@Override
	public void writeBody(XmlSerializer writer) throws IOException {
		if (this.bodyOut != null) {
			this.multiRef = new Vector();
			this.multiRef.addElement(this.bodyOut);
			Object[] qName = getInfo(null, this.bodyOut);
			writer.startTag(this.dotNet ? "" : (String) qName[0],
					(String) qName[1]);
			if (this.dotNet) {
				writer.attribute(null, "xmlns", (String) qName[0]);
			}
			if (this.addAdornments) {
				writer.attribute(null, "id", qName[2] == null ? "o0"
						: (String) qName[2]);
				writer.attribute(this.enc, "root", "1");
			}
			writeElement(writer, this.bodyOut, null, qName[3]);
			writer.endTag(this.dotNet ? "" : (String) qName[0],
					(String) qName[1]);
		}
	}

	@Override
	public void writeObjectBody(XmlSerializer writer, KvmSerializable obj)
			throws IOException {
		int cnt = obj.getPropertyCount();
		
		PropertyInfo propertyInfo = new PropertyInfo();
		for (int i = 0; i < cnt; i++) {
			Object prop = obj.getProperty(i);

			obj.getPropertyInfo(i, this.properties, propertyInfo);
			if (!(prop instanceof SoapObject)) {
				if ((propertyInfo.flags & 0x1) == 0) {
					writer.startTag(propertyInfo.namespace, propertyInfo.name);
					writeProperty(writer, obj.getProperty(i), propertyInfo);
					writer.endTag(propertyInfo.namespace, propertyInfo.name);
				}
			} else {
				SoapObject nestedSoap = (SoapObject) prop;

				Object[] qName = getInfo(null, nestedSoap);
				String namespace = (String) qName[0];
				String type = (String) qName[1];
				String name;
				String prefix = "";
				if ((propertyInfo.name != null)
						&& (propertyInfo.name.length() > 0)) {
					name = propertyInfo.name;
				} else {
					name = (String) qName[1];
				} 
				if(!"".equals(name) && (!"PROMOCIONES_AP".equalsIgnoreCase(name)))
				{
					writer.startTag(this.dotNet ? "" : namespace,name);
					prefix = writer.getPrefix(namespace, true);		
				}
				if(!"".equals(type))
				{	//caso especial solo para pedido, estoy con el fin de moldear el request de EnviarPedido
					//ya que este es diferente a pattern de los demas
					if( (!"PROMOCIONES_AP".equalsIgnoreCase(name)) && (!"PromocionesAplicadas".equals(nestedSoap.getName())) && (!"DetallePedido".equals(nestedSoap.getName())) && (!"Detalles".equals(nestedSoap.getName()) && nestedSoap.getPropertyCount()<=5))
						writer.attribute(this.xsi, "type", prefix + ":" +type );
				} 
				writeObjectBody(writer, nestedSoap);
				if(!"".equals(name) && (!"PROMOCIONES_AP".equalsIgnoreCase(name)))
					writer.endTag(this.dotNet ? "" : namespace,name);
			}
		}
	}

	// super.writeObjectBody(writer, obj);v
	@Override
	protected void writeProperty(XmlSerializer writer, Object obj,
			PropertyInfo type) throws IOException {
		if (obj == null) {
			writer.attribute(this.xsi, this.version >= 120 ? "nil" : "null","true");
			return;
		}
		Object[] qName = getInfo(null, obj);
		if ((type.multiRef) || (qName[2] != null)) 
		{
			int i = this.multiRef.indexOf(obj);
			if (i == -1) {
				i = this.multiRef.size();
				this.multiRef.addElement(obj);
			}
			writer.attribute(null, "href", "#" + qName[2]);
		} else {
			if ((!this.implicitTypes) || (obj.getClass() != type.type)) {
				String prefix = writer.getPrefix((String) qName[0], true);
				writer.attribute(this.xsi, "type", prefix + ":" + qName[1]);
			}
			writeElement(writer, obj, type, qName[3]);
		}
	}

	private void writeElement(XmlSerializer writer, Object element,
			PropertyInfo type, Object marshal) throws IOException {
		if (marshal != null) {
			((Marshal) marshal).writeInstance(writer, element);
		} else if ((element instanceof SoapObject)) {
			this.writeObjectBody(writer, (SoapObject) element);
		} else if ((element instanceof KvmSerializable)) {
			this.writeObjectBody(writer, (KvmSerializable) element);
		} else if ((element instanceof Vector)) {
			this.writeVectorBody(writer, (Vector) element, type.elementType);
		} else {
			throw new RuntimeException("Cannot serialize: " + element);
		}
	}

	protected void writeVectorBody(XmlSerializer writer, Vector vector,
			PropertyInfo elementType) throws IOException {
		String itemsTagName = "item";
		String itemsNamespace = null;
		if (elementType == null) {
			elementType = PropertyInfo.OBJECT_TYPE;
		} else if (((elementType instanceof PropertyInfo))
				&& (elementType.name != null)) {
			itemsTagName = elementType.name;
			itemsNamespace = elementType.namespace;
		}
		int cnt = vector.size();
		Object[] arrType = getInfo(elementType.type, null);
		if (!this.implicitTypes) {
			writer.attribute(this.enc, "arrayType",
					writer.getPrefix((String) arrType[0], false) + ":"
							+ arrType[1] + "[" + cnt + "]");
		}
		boolean skipped = false;
		for (int i = 0; i < cnt; i++) {
			if (vector.elementAt(i) == null) {
				skipped = true;
			} else {
				writer.startTag(itemsNamespace, itemsTagName);
				if (skipped) {
					writer.attribute(this.enc, "position", "[" + i + "]");
					skipped = false;
				}
				writeProperty(writer, vector.elementAt(i), elementType);
				writer.endTag(itemsNamespace, itemsTagName);
			}
		}
	}
}
