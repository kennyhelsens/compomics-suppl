# define dataset as variable d
d<-nature10098.s5.txt.context.category

# print summary stats
sink(file="../reports/summary_stats.txt")
for(i in 10:20){
  print(colnames(d)[i])
  print(summary(d[,i]))
  flush.console()
}
sink(NULL)

table(d$natcategory)

# print summary graphs
png(file = "../graphs/density_summary_graph.png", width=1200, height=1000)
par(mfrow=c(4,3))
for(i in 10:20){
  plot(density(log10(d[,i]), na.rm=T), main="")
  title(paste(c("density plot", colnames(d)[i])))
  print(paste(c("drawing plot", i)))
  flush.console()
}
dev.off()

# print nat boxplots
png(file = "../graphs/nat_boxplots_graph.png", width=1200, height=1000)
par(mfrow=c(4,3))
for(i in 10:20){
  
  boxplot(log10(d[,i]) ~ d[,31], main="")
  
  
  
  title(paste(c("density plot", colnames(d)[i])))
  print(paste(c("drawing plot", i)))
  flush.console()
}
dev.off()

folder <- "../graphs/"

doPlot <- function (i) {
  cname <- colnames(d)[i]
  filename <- paste(folder, cname, ".png", sep="")
  png(file=filename)
  
  e <- cbind(d[i], d[,31])
    
  colnames(e)<-c("a","b")
  p <- ggplot(data=e, aes(x=a, colour=b))
  p <- p + geom_density()
  p <- p + theme_bw()
  p <- p + scale_x_log10()
  p <- p + opts(title = cname)
  p
  dev.off()
#   return(p)
  }

folder <- "../graphs/"

for(i in 10:29) {
  print(i)
  cname <- colnames(d)[i]
  filename <- paste(folder, cname, ".png", sep="")
#    i <- 20
  
  e <- cbind(d[i], d[,31])
    
  colnames(e)<-c("a","b")
  
  min  <- quantile(e$a, 0.001, na.rm=TRUE)
  max  <- quantile(e$a, 0.999, na.rm=TRUE)
  
  p <- ggplot(data=e, aes(x=a, colour=b))
  p <- p + geom_density(adjust=1.5)
  p <- p + theme_bw()
  p <- p + scale_x_log10(limits=c(min,max))
  p <- p + opts(title = cname)
  p
  ggsave(filename)
  
  dev.off()
  
  flush.console()
#   return(p)
}

  
# protein concentration / protein stability

d.old<-d
d<-data.frame(rbind(d[d$nat=="NATA",],d[d$nat=="NATB",],d[d$nat=="NATC",],d[d$nat=="FREE",]))

f <- cbind(d[14], d[20], d[,31], log10(d[14]), log10(d[20]))
colnames(f)<-c("conc","halflife","nat", "log10.conc", "log10.halflife")
# f$nat<-factor(f$nat, levels=c("NATA","NATB","NATC","FREE","OTHER","MISSING"))
f$nat<-factor(f$nat, levels=c("NATA","NATB","NATC","FREE"))

nat.cols<-c(pal.tomoka[13],pal.tomoka[12],pal.tomoka[14],pal.tomoka[2],pal.tomoka[15],pal.tomoka[20])
conc_lim<-c(quantile(f$conc, 0.001, na.rm=TRUE), quantile(f$conc, 0.999, na.rm=TRUE))
halflife_lim <- c(quantile(f$halflife, 0.001, na.rm=TRUE), quantile(f$halflife, 0.999, na.rm=TRUE))  
  
  p <- ggplot(data=f, aes(x=halflife,y=conc, colour=nat))
  p <- p + geom_point(adjust=1.5, size=3, alpha=0.5)
  p <- p + scale_colour_manual(values = nat.cols) 
  p <- p + theme_bw()
  p <- p + scale_x_log10("average protein half life", limits=halflife_lim, breaks=c(10,100,1000))
  p <- p + scale_y_log10("average protein concentration", limits=conc_lim, breaks=c(1000,10000,100000,1000000))
  p <- p + opts(title = "")
  p <- p + opts(legend.key = theme_rect(colour="white", fill = 'white', linetype=1))
  p <- p + opts(strip.background = theme_rect(colour = 'white', fill = 'white', size = 0, linetype='dashed'))
  p <- p + geom_hline(yintercept=10000, linetype=3, alpha=0.8)
  p <- p + geom_vline(xintercept=100, linetype=3, alpha=0.8)
  p<- p + facet_wrap(~nat, ncol=2)
   p <- p + geom_smooth()
  p


# Lorentz curves
par(mfcol=c(1,1))
xlims<-c(0,1)
ylims<-c(0,1)


lc.cols<-c(nat.cols[1], nat.cols[2], nat.cols[3], nat.cols[4])
dlc<-tapply(sqrt(f$conc), f$nat, FUN=Lc)
lc.lty<-1
plot(dlc$NATC, col=lc.cols[1], xlim=xlims, ylim=ylims, sub="Protein concentration", lwd=5, lty=lc.lty)
lines(dlc$NATA, col=lc.cols[2], lwd=5,lty=lc.lty)
lines(dlc$NATB, col=lc.cols[3], lwd=5, lty=lc.lty)
lines(dlc$FREE, col=lc.cols[4], lwd=5, lty=lc.lty)
legend("topleft",legend=c("NATC","NATA","NATB","FREE"), lty=1, lwd=5,col = lc.cols[1:4])

# split f by nats
f.split<-split(x=f, f=f$nat)

# draw qqplots
f.split<-split(x=f, f=f$nat)
par(mfrow=c(2,2))
par(mex=0.75)

myqqplot<-function(x, title){
  qqplot(x, rnorm(length(x), mean(x), sd(x)), pch=1,col=rgb(50,50,50,150,maxColorValue=255), main=title, xlim=c(0,4), ylim=c(0,4), ylab="", xlab="10log protein halflife")
  abline(0,1)
}
myqqplot(f.split$NATA$log10.halflife, "NATA")
myqqplot(f.split$NATB$log10.halflife, "NATB")
myqqplot(f.split$NATC$log10.halflife, "NATC")
myqqplot(f.split$FREE$log10.halflife, "FREE")

# t-tests
i.free<-which(f$nat=="FREE")
i.nata<-which(f$nat=="NATA")
i.natb<-which(f$nat=="NATB")
i.natc<-which(f$nat=="NATC")
m<-f

myttest<-function(m,a,b){
  mf<-m[a,]
  mf<-rbind(mf, m[b,])
  mf<-data.frame(mf, c(rep("A", length(a)), rep("B", length(b))))
  mf$nat<-factor(mf$nat)
  colnames(mf)<-c(colnames(mf)[1:NCOL(mf)-1], "testlab")
  
  head(mf$testlab)
  
  print(levels(mf$nat))
#   wt<-wilcox_test(log10.halflife ~ testlab, data=mf, distribution=approximate(B = 999), conf.int = TRUE,  alternative = "g")
  wt<-t.test(log10.halflife ~ testlab, data=mf, conf.int = TRUE,  alternative = "g")

  
#  c(pvalue(wt)[1], names(statistic(wt))[1])
#    return(c(pvalue(wt)[1], names(statistic(wt))[1]))
   return(wt$p.value)

}

tests<-c()
tests<-rbind(tests,c("FREE","NATA",myttest(f, i.free, i.nata)))
tests<-rbind(tests,c("FREE","NATB",myttest(f, i.free, i.natb)))
tests<-rbind(tests,c("FREE","NATC",myttest(f, i.free, i.natc)))

tests<-rbind(tests,c("NATA","FREE",myttest(f, i.nata, i.free)))
tests<-rbind(tests,c("NATA","NATB",myttest(f, i.nata, i.natb)))
tests<-rbind(tests,c("NATA","NATC",myttest(f, i.nata, i.natc)))

tests<-rbind(tests,c("NATB","FREE",myttest(f, i.natb, i.free)))
tests<-rbind(tests,c("NATB","NATA",myttest(f, i.natb, i.nata)))
tests<-rbind(tests,c("NATB","NATC",myttest(f, i.natb, i.natc)))

tests<-rbind(tests,c("NATC","FREE",myttest(f, i.natc, i.free)))
tests<-rbind(tests,c("NATC","NATA",myttest(f, i.natc, i.nata)))
tests<-rbind(tests,c("NATC","NATB",myttest(f, i.natc, i.natb)))

tests<-data.frame(tests)
colnames(tests)=c("SetA", "SetB", "p-value")
tests

