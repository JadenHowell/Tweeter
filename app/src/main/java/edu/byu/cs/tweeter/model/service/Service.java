package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import edu.byu.cs.tweeter.shared.service.request.Request;
import edu.byu.cs.tweeter.shared.service.response.Response;

public abstract class Service {
    ServerFacade serverFacade;

    /**
     * Template method for all service classes. Takes in a request, accesses a facade, and returns
     * the result. Child classes implement accessFacade(request) and onSuccess(response)
     * @param request
     * @return
     */
    public Response serve(Request request) throws IOException {
        serverFacade = getServerFacade();
        Response response = accessFacade(request);
        if (response.isSuccess()){
            onSuccess(response);
        }
        return response;
    }

    /**
     * accessFacade is private function to be implemented by subclasses of Service, defining
     * how they will access the server facade
     * @param request a request to send to the server facade
     * @return the response returned by the facade
     */
    abstract Response accessFacade(Request request);

    /**
     * abstract class to be impemented by subclasses of Service. When response is successful
     * this will be called
     * @param response the successful response from the server facade
     */
    abstract void onSuccess(Response response) throws IOException;

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their ServerFacade instance from this
     * method to allow for proper mocking.
     *
     * @return the instance.
     */
    ServerFacade getServerFacade() {
        return new ServerFacade();
    }

}
