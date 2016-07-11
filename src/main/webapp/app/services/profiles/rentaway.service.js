(function() {
    'use strict';

    angular
        .module('rentApp')
        .factory('RenatawayService', RentawayService);

    RentawayService.$inject = ['$q', '$http'];

    function RentawayService($q, $http) {

        var dataPromise;

        var service = {
            getRentawayInfo : getRentawayInfo
        };

        return service;

        function getRentawayInfo() {
            if (angular.isUndefined(dataPromise)) {
                dataPromise = $http.get('api/rentaway-profile-info').then(function(result) {
                	var response = {};
                	response.searchkey = result.data.googleSearchKey;
                	return response;
                });
            }
            return dataPromise;
        }
    }
})();
