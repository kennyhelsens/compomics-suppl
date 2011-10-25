
# plot(1,1, col="white", xlim=c(1,ncols), ylim=c(0,1))
# for(i in 1:ncols){
#   plotLine(i, mycols[i])
# }
# 
# plotLine <- function (i, mycol) {
#   lines(x=c(i,i), y=c(0.2,1), lwd=30, col=mycol)
#   text(x=i, y=0.1,labels=c(i), col=mycol)
# }


#http://colrd.com/discover/palette/21894.php
pal.tomoka<-c(rgb(255,20,23,255, maxColorValue=255),
  rgb(255,102,17,255, maxColorValue=255),
  rgb(255,136,68,255, maxColorValue=255),
  rgb(255,238,85,255, maxColorValue=255),
  rgb(254,254,56,255, maxColorValue=255),
  rgb(255,255,153,255, maxColorValue=255),
  rgb(170,204,34,255, maxColorValue=255),
  rgb(187,221,119,255, maxColorValue=255),
  rgb(200,207,130,255, maxColorValue=255),
  rgb(146,167,126,255, maxColorValue=255),
  rgb(85,153,238,255, maxColorValue=255),
  rgb(0,136,204,255, maxColorValue=255),
  rgb(34,102,136,255, maxColorValue=255),
  rgb(23,82,121,255, maxColorValue=255),
  rgb(85,119,119,255, maxColorValue=255),
  rgb(221,187,51,255, maxColorValue=255),
  rgb(211,167,109,255, maxColorValue=255),
  rgb(169,131,75,255, maxColorValue=255),
  rgb(170,102,136,255, maxColorValue=255),
  rgb(118,118,118,255, maxColorValue=255))


mycols<-pal.tomoka
ncols<-length(mycols)
