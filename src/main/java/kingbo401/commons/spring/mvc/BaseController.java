package kingbo401.commons.spring.mvc;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import kingbo401.commons.spring.mvc.editor.DateTimeEditor;
import kingbo401.commons.spring.mvc.editor.StringHtmlEscapeEditor;
import kingbo401.commons.spring.mvc.editor.TimestampEditor;

public abstract class BaseController implements IController {

	protected final static String REDIRECT = "redirect:";
	protected final static String FORWARD = "forward:";

	protected static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";
	protected static final String DATE_TIME_FORMATTER_SLASH = "yyyy/MM/dd HH:mm:ss";
	protected static final String DATE_FORMATTER = "yyyy-MM-dd";
	protected static final String DATE_FORMATTER_SLASH = "yyyy/MM/dd";

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringHtmlEscapeEditor());

		SimpleDateFormat format1 = new SimpleDateFormat(DATE_FORMATTER);
		format1.setLenient(false);
		SimpleDateFormat format2 = new SimpleDateFormat(DATE_TIME_FORMATTER);
		format2.setLenient(false);
		SimpleDateFormat format3 = new SimpleDateFormat(DATE_FORMATTER_SLASH);
		format3.setLenient(false);
		SimpleDateFormat format4 = new SimpleDateFormat(DATE_TIME_FORMATTER_SLASH);
		format4.setLenient(false);
		List<SimpleDateFormat> formats = new ArrayList<SimpleDateFormat>();
		formats.add(format1);
		formats.add(format2);
		formats.add(format3);
		formats.add(format4);

		binder.registerCustomEditor(Date.class, new DateTimeEditor(formats, true));
		binder.registerCustomEditor(Timestamp.class, new TimestampEditor(formats, true));
	}

}
