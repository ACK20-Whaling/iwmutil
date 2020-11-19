#!/bin/bash

# Output KML header
cat<<EOF
<?xml version="1.0" encoding="UTF-8"?> 
<kml xmlns="http://earth.google.com/kml/2.0">
<Document>
<Placemark> 
 <LineString>
  <coordinates>
EOF

# Read in CSV and append a zero altitude to each line  
sed s/$/,0.0/ yourCSV.csv 

cat<<EOF
  </coordinates>
 </LineString>
</Placemark>
</Document>
</kml>
EOF
