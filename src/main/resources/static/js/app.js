var app = angular.module("UrlShortenerApp", []);

app.controller("HomeController", function ($scope, $http) {
    $scope.personalized = false;

    $scope.generate = function() {
        if (!$scope.fullURL) {
            return;
        }
        console.log($scope.customShortUrl);
        $scope.shorternURL = angular.isDefined($scope.customShortUrl) ? $scope.customShortUrl : "";


        $http.post("/rest/url?fullUrl=" + $scope.fullURL + "&customUrl=" + $scope.shorternURL)
            .then(function(response) {
                console.log(response);
                $scope.shorternURL = response.data.url;
            }, function (error) {
                console.log(error);
            })

    };

    $scope.getFull = function() {
        if (!$scope.fullURL) {
            return;
        }

        $http.get("/rest/url?shortUrl=" + $scope.fullURL)
            .then(function(response) {
                console.log(response);
                $scope.shorternURL = response.data.url;
            });
    }
});
