'use strict';

/**
 * @ngdoc function
 * @name yoAngularApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the yoAngularApp
 */
angular.module('yoAngularApp')
  .controller('MainCtrl', ['$scope','restservice', function ($scope,restservice) {
    $scope.newMSAAccount = {};

    $scope.createMSAAccount = function(){
      restservice.createMSAAccount($scope.newMSAAccount,function(data){
        console.log('created ' + data);
      });
    };

    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  }]);
