package edu.byu.cs.tweeter.client.model.services.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.services.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.Status;

public class GetStatusesHandler extends PagedHandler<Status> {
    public GetStatusesHandler(PagedObserver<Status> observer) {
        super(observer);
    }
}