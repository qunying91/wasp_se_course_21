package jpa;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

public class DbExecuteContext extends CustomExecutionContext {

    @Inject
    public DbExecuteContext(ActorSystem actorSystem) {
        super(actorSystem, "database.dispatcher");
    }
}
