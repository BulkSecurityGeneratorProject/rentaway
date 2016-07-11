(function() {
    'use strict';

    angular
        .module('rentApp')
        .controller('CountryDetailController', CountryDetailController);

    CountryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Country'];

    function CountryDetailController($scope, $rootScope, $stateParams, entity, Country) {
        var vm = this;

        vm.country = entity;

        var unsubscribe = $rootScope.$on('rentApp:countryUpdate', function(event, result) {
            vm.country = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
