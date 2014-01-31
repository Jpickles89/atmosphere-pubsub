
var controller = angular.module('angular.atmosphere.chat.controllers', []);

controller.controller('chatController', function ($scope) {
    var socket;

    $scope.model = {
        transport: 'long-polling',
        messages: []
    };

    var request = {
        contentType: "application/json",
        logLevel: 'debug',
        transport: 'websocket',
        fallbackTransport: 'long-polling'
    };

    request.onOpen = function(response) {
        $scope.$apply(function() {
            $scope.model.transport = response.transport;
            $scope.model.connected = true;
            $scope.model.content = 'Atmosphere connected using ' + response.transport;
        });
    };

    request.onClientTimeout = function(response){
        $scope.model.content = 'Client closed the connection after a timeout. Reconnecting in ' + request.reconnectInterval;
        $scope.model.connected = false;
        socket.push(JSON.stringify({ topic: $scope.model.topic, message: 'is inactive and closed the connection. Will reconnect in ' + request.reconnectInterval }));

        setTimeout(function(){
            socket = atmosphere.subscribe(request);
        }, request.reconnectInterval);
    };

    request.onReopen = function(response){
        $scope.$apply(function() {
            $scope.model.connected = true;
            $scope.model.content = 'Atmosphere re-connected using ' + response.transport;
        })
    };

    //For demonstration of how you can customize the fallbackTransport using the onTransportFailure function
    request.onTransportFailure = function(errorMsg, request){
        $scope.$apply(function() {
            alert(errorMsg);
            request.fallbackTransport = 'long-polling';
            $scope.model.header = 'Atmosphere Chat. Default transport is WebSocket, fallback is ' + request.fallbackTransport;
        })
    };

    request.onClose = function(response){
        $scope.$apply(function() {
            $scope.model.connected = false;
            $scope.model.content = 'Server closed the connection after a timeout';
            socket.push(JSON.stringify({ topic: $scope.model.topic, message: 'disconnecting' }));
        })
    };

    request.onError = function(response){
        $scope.model.content = "Sorry, but there's some problem with your socket or the server is down";
        $scope.model.logged = false;
    };

    request.onReconnect = function(request, response){
        $scope.model.content = 'Connection lost. Trying to reconnect ' + request.reconnectInterval;
        $scope.model.connected = false;
    };

    request.onMessage = function(response) {
        $scope.$apply(function(){
            var responseText = response.responseBody;
            try{
                var message = JSON.parse(responseText);
                $scope.model.messages.push({topic: message.topic, message: message.message});

            }catch(e){
                console.error("Error parsing JSON: ", responseText);
                throw e;
            }
        })
    };

    $scope.connect = function(topic){
        request.url = "api/entity/" + topic,
        $scope.model.topic = topic;
        socket = atmosphere.subscribe(request);
    }

    var inputTopic = $('#input-topic');
    var inputMessage = $('#input-message');

    inputTopic.keydown(function(event){
        var topic = this;
        var msg = $(topic).val();
        if(msg && msg.length > 0 && event.keyCode === 13){

            $scope.$apply(function(){
                if(!$scope.model.topic)
                    $scope.connect(msg);
                $(topic).val('');
            });
        }
    });

    inputMessage.keydown(function(event){
        var message = this;
        var msg = $(message).val();
        if(event.keyCode === 13){
            $scope.$apply(function(){
                socket.push(JSON.stringify({topic: $scope.model.topic, message: msg}));
                $(message).val('');
            });
        }
    });
});