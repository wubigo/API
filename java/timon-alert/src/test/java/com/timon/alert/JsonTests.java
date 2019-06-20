package com.timon.alert;

import com.timon.common.DataUtil;
import com.timon.common.HttpUtil;
import com.timon.domain.Group550;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@JsonTest
@Slf4j
public class JsonTests {

    @Autowired
    private JacksonTester<Group550> json;

    @Test
    public void testDeserialize() throws Exception {

        String content = HttpUtil.readUrl("http://172.16.16.1/TiMon/core/raw/dev/avm/src/main/resources/rawData.json");

        assertThat(this.json.parseObject(content).getNbiot_type()).isEqualTo("group550");
    }
}
