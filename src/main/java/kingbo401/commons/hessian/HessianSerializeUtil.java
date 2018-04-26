package kingbo401.commons.hessian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

public class HessianSerializeUtil {

	private static final SerializerFactory _serializerFactory = new SerializerFactory();

	public static byte[] serialize(Object obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		Hessian2Output h2o = new Hessian2Output(bos);
		h2o.setSerializerFactory(_serializerFactory);
		h2o.setCloseStreamOnClose(true);
		try {
			h2o.writeObject(obj);

		} catch (IOException e) {
			throw new RuntimeException("serialize object error:", e);
		} finally {
			try {
				h2o.flush();
				h2o.close();
			} catch (IOException e) {
				throw new RuntimeException("serialize object error:", e);
			}
		}
		return bos.toByteArray();
	}

	@SuppressWarnings("unchecked")
	public static <T> T  deserialize(byte[] bytes) {
		if(bytes == null) return null;
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		Hessian2Input hio = new Hessian2Input(bis);
		hio.setSerializerFactory(_serializerFactory);
		hio.setCloseStreamOnClose(true);
		try {
			Object result = hio.readObject();
			if(result != null)
				return (T) result;
			else return null;
		} catch (IOException e) {
			throw new RuntimeException("deserialize object error:", e);
		} finally {
			try {
				hio.close();
			} catch (IOException e) {
				throw new RuntimeException("deserialize object error:", e);
			}
		}
	}
}
