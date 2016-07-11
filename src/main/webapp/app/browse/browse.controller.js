(function() {
    'use strict';

    angular
        .module('rentApp')        
        .controller('BrowseController', BrowseController);


    BrowseController.$inject = [ '$scope', '$state', 'House', 'HouseSearch'];

    function BrowseController ($scope, $state, House, HouseSearch) {
    	console.log('Browse');
    	 var vm = this;
         
         vm.houses = [];
         vm.search = search;
         vm.loadAll = loadAll;

         loadAll();

         function loadAll() {
             House.query(function(result) {
                 vm.houses = result;
             });
         }

         function search () {
             if (!vm.searchQuery) {
                 return vm.loadAll();
             }
             HouseSearch.query({query: vm.searchQuery}, function(result) {
                 vm.houses = result;
             });
         }    
    }
})();
