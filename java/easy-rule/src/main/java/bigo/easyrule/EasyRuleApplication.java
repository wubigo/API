package bigo.easyrule;

import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class EasyRuleApplication  implements CommandLineRunner {

    public static void main(String[] args) {



        SpringApplication app = new SpringApplication(EasyRuleApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        Group550 g = new Group550();
        g.percentPacketLoss = 30;
        Facts facts = new Facts();
        facts.put("group550", g);
        facts.put("percentPacketLoss", 40);
        Rules rules = new Rules();
        rules.register(new Group550Rule());
        // fire rules on known facts
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(rules, facts);
        facts.asMap().forEach( (k, v) -> log.debug(k + ":" + v) );
    }

}
