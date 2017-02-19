for f in `ls *.cpp *.cxx *.hxx *.h`
do
  cat $f | sed "s/com_etm_api/at_rocworks_oc4j/g" |  
           sed "s/com\.etm\.api/at.rocworks.oc4j/g" |
           sed "s/com\/etm\/api/at\/rocworks\/oc4j/g" > tmp
  mv tmp $f
done

for f in `ls com_etm_api_*.cpp com_etm_api_*.h`
do
  n=`echo $f | sed "s/com_etm_api/at_rocworks_oc4j/"`
  echo $n
  mv $f $n
done
