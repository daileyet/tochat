package cc.tochat.webserver;

import cc.tochat.webserver.helper.json.AnnotationExclusionStrategy;

import com.google.gson.GsonBuilder;
import com.openthinks.easyweb.annotation.configure.EasyConfigure;
import com.openthinks.easyweb.annotation.configure.RequestSuffixs;
import com.openthinks.easyweb.annotation.configure.ScanPackages;
import com.openthinks.easyweb.context.Bootstrap;
import com.openthinks.easyweb.utils.json.OperationJson;
import com.openthinks.libs.sql.dhibernate.support.SessionFactory;
import com.openthinks.libs.sql.lang.Configurator;
import com.openthinks.libs.sql.lang.ConfiguratorFactory;
import com.openthinks.libs.utilities.logger.ProcessLogger;

@EasyConfigure
@ScanPackages({ "cc.tochat.webserver" })
@RequestSuffixs(".do,.htm")
public class EasyWebConfigure implements Bootstrap {

	@Override
	public void cleanUp() {
	}

	@Override
	public void initial() {
		ProcessLogger.currentLevel = ProcessLogger.PLLevel.DEBUG;
		OperationJson.setGsonInstance(new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy())
				.create());

		Configurator configuration = ConfiguratorFactory.getInstance(getClass().getResourceAsStream(
				"/dbconfig.properties"));
		SessionFactory.setDefaultConfigurator(configuration);
	}
}
