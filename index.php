<!DOCTYPE html>
<html lang="en">

<head>

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">
  <link rel="shurtcut icon"  href="https://image.flaticon.com/icons/png/512/36/36447.png" type="image/png">
  <title>Sharing bicycle</title>

  <!-- Bootstrap core CSS -->
  <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

  <!-- Custom styles for this template -->
  <link href="css/scrolling-nav.css" rel="stylesheet">

   <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
  <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
 <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
 <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

</head>

<body id="page-top">

  <!-- Navigation -->
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top" id="mainNav">
    <div class="container">
      <a class="navbar-brand js-scroll-trigger" href="#page-top">Welcome</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarResponsive">
        <ul class="navbar-nav ml-auto">
          <li class="nav-item">
            <a class="nav-link js-scroll-trigger" href="#about">Search</a>
          </li>
          <li class="nav-item">
            <a class="nav-link js-scroll-trigger" href="#services">Resultats</a>
          </li>
          <li class="nav-item">
            <a class="nav-link js-scroll-trigger" href="#statistics">Statistics</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>

  <header class="bg-primary text-white">
    <div class="container text-center">
      <h1>Semantic Web Project </h1>
      <p class="lead">A project to Build a database of bicycle-sharing stations</p>
    </div>
  </header>

  <section id="about">
    <div class="container">
      <div class="row">
        <div class="col-lg-8 mx-auto">
          <h2>Search</h2>
            <?php
        echo '<form action="#" method="post">';
        echo '<div class="form-row" class="form-group">';
        echo '<div class="col-3">';
        echo '<p>Choose your city</p>';
        echo '</div>';
        echo '<div class="col-4">';
        echo '<select class="form-control" id="exampleFormControlSelect1" name="City[]" >';
        echo "<option>lyon</option>";
        echo "<option>marseille</option>";
        echo "<option>mulhouse</option>";
        echo "<option>rouen</option>";
        echo "<option>toulouse</option>";
        echo '</select>';
        echo '</div>';
        echo '<ul class="navbar-nav ml-auto">';   
        echo ' <li class="nav-item">';
        echo '<input type="submit" name="submit" class="btn btn-outline-dark" value="Search"  onClick="location.href = #services">';
        echo ' </li>';
        echo ' </ul>';
          echo '<ul class="navbar-nav ml-auto">';   
        echo ' <li class="nav-item">';
         echo '<input type="submit" name="submit1" class="btn btn-outline-dark" value="Statistic"  onClick="location.href = #statistics">';
         echo ' </li>';
        echo ' </ul>';
        echo '</div>';      
        echo '</form>';
         
          ?>
        </div>
      </div>
    </div>
  </section>

  <section id="services" class="bg-light">
    <div class="container">
      <div class="row">
        <div class="col-lg-8 mx-auto">
          <h2>Results</h2>
          <?php
if(isset($_POST['submit'])){

  global $select;
foreach ($_POST['City'] as $select)
{
 echo "You have selected :" .$select;
 ?>
  <?php
  /* ARC2 static class inclusion */ 
  
  include_once('C:\wamp64\www\Semantic Web\semsol\ARC2.php');  
 
  $dbpconfig = array(
  "remote_store_endpoint" => "http://localhost:3030/bicycleSharing",
   );
  // instanciation
  $store = ARC2::getRemoteStore($dbpconfig); 
 
  if ($errs = $store->getErrors()) {
     echo "<h1>getRemoteSotre error<h1>" ;
  }

  $query = "
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
prefix j.0:   <http://www.w3.org/2000/01/rdf-schema/> 
SELECT ?name ?adress ?status ?bikeAvail ?standAvail ?ville ?time ?description
    where {
    ?STATION    j.0:IN     ?ville .
    ?STATION    j.0:Has_Name ?name .
    ?STATION    j.0:Has_adress ?adress .
    ?STATION    j.0:Has_status ?status .
    ?STATION    j.0:Has_bike_AV ?bikeAvail .
    ?STATION    j.0:Has_stand_AV ?standAvail . 
    ?STATION    j.0:Has_last_update  ?time .
    ?STATION   j.0:contains ?weather.
    ?weather    j.0:Has_weather ?description .
    FILTER(?ville = '$select')
}ORDER BY DESC(?bikeAvail)
";


  /* execute the query */
  $rows = $store->query($query, 'rows'); 
 
    if ($errs = $store->getErrors()) {
       echo "Query errors" ;
       print_r($errs);
    }
 
    /* display the results in an HTML table */
    echo "<table class='table table-hover' ns='<http://www.w3.org/2000/01/rdf-schema/>' xmlns:v='http://www.w3.org/2006/vcard/ns#' xmlns='http://www.w3.org/1999/xhtml' xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' xmlns:rdfs='http://www.w3.org/2000/01/rdf-schema#'
    about='http://'+list.get(i)+'.org/'>
    <thead about='http://station_'+number+'_'+ville+'.org/' typeof='http://station.org/'>
    <tr>
        <th scope='col' >#</th> 
        <th scope='col' property='ns:Has_Name' datatype='xsd:String'>StationName </th>
        <th scope='col' property='ns:Has_adress' property='v:street-address' datatype='xsd:String' >Address</th>
        <th scope='col' property='ns:Has_status' datatype='xsd:String'>Status</th>
        <th scope='col' property='ns:Has_bike_AV' datatype='xsd:integer'>Bike Available</th>
        <th scope='col' property='ns:Has_stand_AV' datatype='xsd:integer'>Stand Available</th>  
        <th scope='col' property='ns:IN' property='v:locality' datatype='xsd:String'>City</th>
        <th scope='col' property='ns:Has_last_update' datatype='xsd:date'>Time</th>
        <th scope='col' property='ns:Has_weather' datatype='ns:weather'>Weather</th>

        </tr>
    </thead>";
   $id=0;
    /* loop for each returned row */
    foreach( $rows as $row ) { 
    echo "<tr><td>".++$id. "</td>
    <td>".$row['name']."</td><td>" . 
    $row['adress']. "</td><td>" . 
    $row['status']. "</td><td>" . 
    $row['bikeAvail']. "</td><td>" . 
    $row['standAvail']. "</td><td>" . 
    $row['ville']. "</td><td>" . 
    date('H:i:s',$row['time'])."</td><td>" . 
    $row['description']. "</td>" ;  
   
    }
 
    echo "</table>" ;
}}
  ?>

          
        </div>
      </div>
    </div>
  </section>


  <section id="statistics">
    <div class="container">
      <div class="row">
        <div class="col-lg-8 mx-auto">
          <h2>Statistics</h2>
   <?php
if(isset($_POST['submit1'])){

  global $select;
foreach ($_POST['City'] as $select)
{
 echo "You have selected :" .$select;
 ?>
  <?php
  /* ARC2 static class inclusion */ 
  
  include_once('C:\wamp64\www\Semantic Web\semsol\ARC2.php');  // il faut mettre le lien ou il existe la laibrairie
 
  $dbpconfig = array(
  "remote_store_endpoint" => "http://localhost:3030/bicycleStore",
   );
  // instanciation
  $store = ARC2::getRemoteStore($dbpconfig); 
 
  if ($errs = $store->getErrors()) {
     echo "<h1>getRemoteSotre error<h1>" ;
  }

  $query = "
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
prefix j.0:   <http://www.w3.org/2000/01/rdf-schema/> 
SELECT ?name ?adress ?status ?bikeAvail ?standAvail ?stands ?time 
    where {
    ?STATION    j.0:IN     ?ville .
    ?STATION    j.0:Has_Name ?name .
    ?STATION    j.0:Has_adress ?adress .
    ?STATION    j.0:Has_status ?status .
    ?STATION    j.0:Has_bike_AV_2 ?bikeAvail .
    ?STATION    j.0:Has_stand_AV_2 ?standAvail . 
    ?STATION    j.0:Has_last_update  ?time .
    ?STATION    j.0:Has_stands_2    ?stands .
    FILTER(?ville = '$select' && ?standAvail = 0 )
}ORDER BY DESC(?bikeAvail)
";


  /* execute the query */
  $rows = $store->query($query, 'rows'); 
 
    if ($errs = $store->getErrors()) {
       echo "Query errors" ;
       print_r($errs);
    }
 
    /* display the results in an HTML table */
    echo "<table class='table table-hover' ns='<http://www.w3.org/2000/01/rdf-schema/>' xmlns:v='http://www.w3.org/2006/vcard/ns#' xmlns='http://www.w3.org/1999/xhtml' xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' xmlns:rdfs='http://www.w3.org/2000/01/rdf-schema#'
    about='http://'+list.get(i)+'.org/'>
    <thead about='http://station_'+number+'_'+ville+'.org/' typeof='http://station.org/'>
    <tr>
        <th scope='col' >#</th> 
        <th scope='col' property='ns:Has_Name' datatype='xsd:String'>StationName </th>
        <th scope='col' property='ns:Has_adress' property='v:street-address' datatype='xsd:String' >Address</th>
        <th scope='col' property='ns:Has_status' datatype='xsd:String'>Status</th>
        <th scope='col' property='ns:Has_bike_AV' datatype='xsd:integer'>Bike Available</th>
        <th scope='col' property='ns:Has_stand_AV' datatype='xsd:integer'>Stand Available</th>  
        <th scope='col' property='ns:IN' property='v:locality' datatype='xsd:String'>Stands</th>
        <th scope='col' property='ns:Has_last_update' datatype='xsd:date'>Time</th>
        </tr>
    </thead>";
   $id=0;
    /* loop for each returned row */
    foreach( $rows as $row ) { 
    echo "<tr><td>".++$id. "</td>
    <td>".$row['name']."</td><td>" . 
    $row['adress']. "</td><td>" . 
    $row['status']. "</td><td>" . 
    $row['bikeAvail']. "</td><td>" . 
    $row['standAvail']. "</td><td>" . 
    $row['stands']. "</td><td>" . 
    date('H:i:s',$row['time'])."</td>" ;  
   
    }
 
    echo "</table>" ;
}}
  ?>
        </div>
      </div>
    </div>
  </section>

  <!-- Footer -->
  <footer class="py-5 bg-dark">
    <div class="container">
      <p class="m-0 text-center text-white">Copyright &copy; 2019</p>
    </div>
    <!-- /.container -->
  </footer>

  <!-- Bootstrap core JavaScript -->
  <script src="vendor/jquery/jquery.min.js"></script>
  <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

  <!-- Plugin JavaScript -->
  <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

  <!-- Custom JavaScript for this theme -->
  <script src="js/scrolling-nav.js"></script>

</body>

</html>
