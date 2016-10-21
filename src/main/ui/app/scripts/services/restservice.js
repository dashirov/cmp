angular.module('yoAngularApp')
.service('restservice',['$http','$q',
function ($http,$q){

  return {
    createMSAAccount : function(msaAccount,success,failure){
      $http.post('/rest/account/createMSA',msaAccount,null).then(success,failure);
    }
  };

}]);
