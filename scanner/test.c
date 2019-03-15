//this is a c example program
/*
preparation
*/
#include<stdio.h>
#include<filesystem>
#define NUM 25
int main(){
    int a=1,b;
    for(int i=0;i<NUM;i++){
        b=i+a;
    }
    double c=12.0;
    c<<=2;
    printf("test program %d",b);
    return 0;
}