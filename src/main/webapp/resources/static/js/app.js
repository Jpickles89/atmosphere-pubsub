'use strict';

// Declare app level module which depends on filters, and services
angular.module('angular.atmosphere.chat', [
    'ngRoute',
    'angular.atmosphere.chat.controllers',
]).config(['$routeProvider', function ($routeProvider) {

    $routeProvider.when('/chat', {
        templateUrl: 'resources/static/partials/chat.html',
        controller: 'chatController'
    });

    $routeProvider.otherwise({redirectTo: '/chat'});
}]);