package dev.eposs.elementsutils.api.timer;

import dev.eposs.elementsutils.feature.excaliburtime.ExcaliburTimeData;

public class ExcaliburTimerApi extends AbstractTimerApi<ExcaliburTimeData> {
    public ExcaliburTimerApi() {
        super(ExcaliburTimeData.class, "https://elements-utils.eposs.dev/api/excalibur?server=$SERVER_ID");
    }
}
