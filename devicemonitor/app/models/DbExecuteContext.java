package models;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

public class DbExecuteContext extends CustomExecutionContext {

    public DbExecuteContext(ActorSystem actorSystem) {
        super(actorSystem, "database.dispatcher");
    }
}
