#define CATCH_CONFIG_MAIN
#include "cs221util/catch.hpp"
#include <vector>
#include <sys/stat.h>
#include <iostream>
#include "cs221util/PNG.h"
#include "cs221util/HSLAPixel.h"
#include "stats.h"
#include "toqutree.h"
#include "cs221util/PNG.h"

using namespace std;
using namespace cs221util;


TEST_CASE("stats::basic rectArea","[weight=1][part=stats]"){

    PNG data; data.resize(2,2);     //Why does this not seg fault?

    stats s(data);
    pair<int,int> ul(0,0);
    pair<int,int> lr(1,1);
    long result = s.rectArea(ul,lr);

    REQUIRE(result == 4);

}

TEST_CASE("stats::basic getAvg","[weight=1][part=stats]"){
    PNG data; data.resize(2,2);
    for (int i = 0; i < 2; i ++){
        for (int j = 0; j < 2; j++){
            HSLAPixel * p = data.getPixel(i,j);
            p->h = 135*j + i * 90;
            p->s = 1.0;
            p->l = 0.5;
            p->a = 1.0;
        }
    }
    stats s(data);
    pair<int,int> ul(0,0);
    pair<int,int> lr(1,1);
    HSLAPixel result = s.getAvg(ul,lr);
    HSLAPixel expected(112.5,1.0, 0.5);

    REQUIRE(result == expected);
}

TEST_CASE("stats::basic buildHist","[weight=1][part=stats]"){
    PNG data; data.resize(2,2);
    for (int i = 0; i < 2; i ++){
        for (int j = 0; j < 2; j++){
            HSLAPixel * p = data.getPixel(i,j);
            p->h = 135*j + i * 90;
            p->s = 1.0;
            p->l = 0.5;
            p->a = 1.0;
        }
    }
    stats s(data);
    pair<int,int> ul(0,0);
    pair<int,int> lr(1,1);
    vector<int> expected(36,0);
    expected[0]++;
    expected[13]++;
    expected[9]++;
    expected[22]++;
    vector<int> result = s.buildHist(ul, lr);
    REQUIRE(result == expected);
}

TEST_CASE("toqutree::basic ctor render","[weight=1][part=toqutree]"){
    PNG img;
    img.readFromFile("images/stanleySquare.png");

    toqutree t1(img,9);

    PNG out = t1.render();
    out.convert();

    REQUIRE(out==img);
}

TEST_CASE("stats::basic entropy","[weight=1][part=stats]"){
    PNG data; data.resize(2,2);
    for (int i = 0; i < 2; i ++){
        for (int j = 0; j < 2; j++){
            HSLAPixel * p = data.getPixel(i,j);
            p->h = 135*j + i * 90;
            p->s = 1.0;
            p->l = 0.5;
            p->a = 1.0;
        }
    }
    stats s(data);
    pair<int,int> ul(0,0);
    pair<int,int> lr(1,1);
    long result = s.entropy(ul,lr);

    REQUIRE(result == 2);
}

TEST_CASE("toqutree::basic copy","[weight=1][part=toqutree]"){
    PNG img;
    img.readFromFile("images/geo.png");

    toqutree t1(img,5);
    toqutree t1copy(t1);

    PNG out = t1copy.render();
    out.writeToFile("images/output-geo.png");

    REQUIRE(out==img);
}

TEST_CASE("toqutree::basic prune","[weight=1][part=toqutree]"){
  // PNG img;
  // img.readFromFile("images/IMG_6953.png");
  // toqutree t1(img, 9);
  // t1.prune(0.05);
  // PNG result = t1.render();
  // result.convert();
  // result.writeToFile("images/prunedpic.png");
    PNG img;
    img.readFromFile("images/ada.png");
    // PNG img2;
    // img2.readFromFile("images/spaceShips.png");

    toqutree t1(img,9);
    // toqutree t2(img2,10);
    // t2.prune(0.05);
    // PNG result2 = t2.render();
    // result2.convert();
    // result2.writeToFile("images/output-spaceShips.png");

    t1.prune(0.05);
    PNG result = t1.render();

    PNG expected; expected.readFromFile("images/adaPrune.05.png");
    result.convert();
    result.writeToFile("images/output-prunetest.png");
    // // PNG img;
    // // img.readFromFile("images/color.png");
    // // toqutree t1(img, 8);
    // // t1.prune(0.05);
    // // PNG result = t1.render();
    // // result.convert();
    // // result.writeToFile("images/output-colortest.png");
    // PNG expected; expected.readFromFile("images/colorprunedXD.png");
    // // int x = t1.size();
    // // double h = result.getPixel(251,107)->h;
    // // double s = result.getPixel(251,107)->s;
    // // double l = result.getPixel(251,107)->l;
    // // double a = result.getPixel(251,107)->a;
    // // cout << h << endl;
    // // cout << s << endl;
    // // cout << l << endl;
    // // cout << a << endl;
    // // cout << x << endl;

    REQUIRE(expected==result);
}
