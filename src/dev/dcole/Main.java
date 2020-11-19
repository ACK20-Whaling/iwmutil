package dev.dcole;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	    // Take in CSV to read from
        String fileName = args[0];

        ArrayList<Double> latitude = new ArrayList<>();
        ArrayList<Double> longitude = new ArrayList<>();

        // Read in latitude and longitude from file (decimal or deg/min/sec)
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] val = line.split(",");

                double lat = 0.0;
                double lon = 0.0;

                // Handle degree - minute - second - direction notation
                if (val[0].contains("'")) {
                    double deg_lat = Double.parseDouble(val[0].substring(0, val[0].indexOf("°")));
                    double deg_lon = Double.parseDouble(val[1].substring(0, val[1].indexOf("°")));

                    double min_lat = Double.parseDouble(val[0].substring(val[0].indexOf("°") + 1, val[0].indexOf("'")));
                    double min_lon = Double.parseDouble(val[1].substring(val[1].indexOf("°") + 1, val[1].indexOf("'")));

                    double sec_lat = Double.parseDouble(val[0].substring(val[0].indexOf("'") + 1, val[0].indexOf("\"")));
                    double sec_lon = Double.parseDouble(val[1].substring(val[1].indexOf("'") + 1, val[1].indexOf("\"")));

                    lat = deg_lat + (min_lat / 60) + (sec_lat / 3600);
                    lon = deg_lon + (min_lon / 60) + (sec_lon / 3600);

                    // Negative if south
                    if (val[0].contains("S")) {
                        lat = -lat;
                    }

                    // Negative if west
                    if (val[1].contains("W")) {
                        lon = -lon;
                    }
                } else if (!val[0].contains("?")) {
                    lat = Double.parseDouble(val[0]);
                    lon = Double.parseDouble(val[1]);
                }

                if (val[0].contains("?")) {
                    latitude.add(null);
                    longitude.add(null);
                } else {
                    latitude.add(lat);
                    longitude.add(lon);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Interpolate missing data points (Can't interpolate missing initial coordinate though!
//        for (int i = 1; i < latitude.size(); i++) {
//            if (latitude.get(i) == null) {
//                // Get last latitude & longitude
//                double last_lat = latitude.get(i - 1);
//                double last_lon = longitude.get(i - 1);
//
//                // Find length of gap, also get next lat & lon
//                int gap_len = 0;
//
//                double next_lat = 0.0;
//                double next_lon = 0.0;
//
//                for (int j = i; j < latitude.size(); j++) {
//                    if (latitude.get(j) == null) {
//                        gap_len++;
//                    } else {
//                        next_lat = latitude.get(j);
//                        next_lon = longitude.get(j);
//                        break;
//                    }
//                }
//
//                // Skip gap if we would have to interpolate across the prime meridian
//                if ((last_lon < 0 && next_lon > 0) || (last_lon > 0 && next_lon < 0)) {
//                    i += (gap_len - 1);
//                    continue;
//                }
//
//                // Fill in gap
//                for (int j = i; j < (i + gap_len); j++) {
//                    // Calculate interpolation scalar
//                    double s = ((double) (j - i + 1)) / ((double) (gap_len + 1));
//
//                    // Interpolate
//                    double lat = last_lat + s * (next_lat - last_lat);
//                    double lon = last_lon + s * (next_lon - last_lon);
//
//                    latitude.set(j, lat);
//                    longitude.set(j, lon);
//                }
//
//                // Skip loop to the end of the gap
//                i += (gap_len - 1);
//            }
//        }

        // Print out coordinates
        for (int i = 0; i < latitude.size(); i++) {
            double lat = latitude.get(i);
            double lon = longitude.get(i);

            if (lon > 0) {
                lon = -180.0 - (180 - lon);
            }
            // Print out latitude & longitude shifted back into proper range
            System.out.println(lat + ", " + lon);
        }
    }
}
