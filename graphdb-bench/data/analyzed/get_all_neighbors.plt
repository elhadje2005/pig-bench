set terminal postscript color enhanced
set output 'get_all_neighbors.eps'

set datafile separator ';'
set xlabel 'Neighborhood Size (vertices)'
set ylabel 'Time (nanoseconds)'

set xrange [0:100]
set yrange [0:3e+06]

plot '<sed "1,3d" bdb-512M-barabasi1M/get_all_neighbors' using 2:1 title 'bdb', \
     '<sed "1,3d" dex-512M-barabasi1M/get_all_neighbors' using 2:1 title 'dex', \
     '<sed "1,3d" dup-512M-barabasi1M/get_all_neighbors' using 2:1 title 'dup', \
     '<sed "1,3d" neo-512M-barabasi1M/get_all_neighbors' using 2:1 title 'neo', \
     '<sed "1,3d" rdf-512M-barabasi1M/get_all_neighbors' using 2:1 title 'rdf'
