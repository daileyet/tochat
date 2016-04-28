package cc.tochat.webserver;

import com.openthinks.easyweb.annotation.configure.EasyConfigure;
import com.openthinks.easyweb.annotation.configure.RequestSuffixs;
import com.openthinks.easyweb.annotation.configure.ScanPackages;
import com.openthinks.easyweb.context.Bootstrap;
import com.openthinks.libs.utilities.logger.ProcessLogger;

@EasyConfigure
@ScanPackages({ "cc.tochat.webserver" })
@RequestSuffixs(".do,.htm")
public class EasyWebConfigure implements Bootstrap {

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initial() {
		ProcessLogger.currentLevel = ProcessLogger.PLLevel.DEBUG;
	}

}
