/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbscan;

/**
 *
 * @author Danar
 */
import java.util.List;
import java.util.LinkedList;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainDBScan
{

    private static List<Point> pointList = new ArrayList<Point>();
    public final static int eps = 7;//epsilon jarak
    public final static int minp = 2;//minimum points
    public List<Point> pointsList = new ArrayList<Point>();//Store dari original DATA.txt
    public static List<List<Point>> hasilAkhirList = new ArrayList<List<Point>>();//Thasil akhir clustering

    public void dbScan() throws IOException {
        pointsList = getPointsList();
        for (int index = 0; index < pointsList.size(); ++index) {
            List<Point> tmpLst = new ArrayList<Point>();
            Point p = pointsList.get(index);
            if (p.isClassed()) {
                continue;
            }
            tmpLst = isCorePoint(pointsList, p, eps, minp);
            if (tmpLst != null) {
                hasilAkhirList.add(tmpLst);
            }
        }
        int length = hasilAkhirList.size();
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                if (i != j) {
                    if (gabungList(hasilAkhirList.get(i), hasilAkhirList.get(j))) {
                        hasilAkhirList.get(j).clear();
                    }
                }
            }
        }
    }

////Mencari jarak antar 2 data
    public double getDistance(Point p, Point q) {
        double dx = p.getX() - q.getX();
        double dy = p.getY() - q.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }

//////Mengecek apakah ada corepoint
    public List<Point> isCorePoint(List<Point> lst, Point p, int e, int minp) {
        int count = 0;
        List<Point> tmpLst = new ArrayList<Point>();
        //utk mengecek apakah iterator mempunyai elemen selanjutnya
        for (Iterator<Point> it = lst.iterator(); it.hasNext();) {
            Point q = it.next();
            if (getDistance(p, q) <= e) {
                ++count;
                if (!tmpLst.contains(q)) {
                    tmpLst.add(q);
                }
            }
        }
        if (count >= minp) {

            p.setKey(true); //menSet Core Point
            return tmpLst;
        }
        return null;
    }

//untuk mengambil data dari DATA.txt
    public List<Point> getPointsList() throws IOException {
        List<Point> lst = new ArrayList<Point>();//stores semua data

        String txtPath = "E:\\TIK UB\\Semester 5\\Pengenalan Pola\\data.txt";

        BufferedReader br = new BufferedReader(new FileReader(txtPath));
        String str = "";
        while ((str = br.readLine()) != null && str != "") {
            lst.add(new Point(str));
        }
        br.close();
        return lst;
    }

//Untuk menggabungkan subcluster menjadi cluster
    public boolean gabungList(List<Point> a, List<Point> b) {
        boolean merge = false;
        if (a == null || b == null) {
            return false;
        }
        for (int index = 0; index < b.size(); ++index) {
            Point p = b.get(index);
            if (p.isKey() && a.contains(p)) {
                merge = true;
                break;
            }
        }
        if (merge) {
            for (int index = 0; index < b.size(); ++index) {
                if (!a.contains(b.get(index))) {
                    a.add(b.get(index));
                }
            }
        }
        return merge;
    }

//////Displays hasilAkhir untuk mengeprint Class///////////////
    public void display(List<List<Point>> resultList) {
        int index = 1;
        for (Iterator<List<Point>> it = resultList.iterator(); it.hasNext();) {
            List<Point> lst = it.next();
            if (lst.isEmpty()) {
                continue;
            }
            System.out.println("-----CLUSTER:-" + index + "-----");
            for (Iterator<Point> it1 = lst.iterator(); it1.hasNext();) {
                Point p = it1.next();
                System.out.println(p.print());
            }
            index++;
        }

    }

    class Point {

        int x;
        int y;
        boolean isKey;
        boolean isClassed;

        public boolean isKey() {
            return isKey;
        }

        public void setKey(boolean isKey) {
            this.isKey = isKey;
            this.isClassed = true;
        }

        public boolean isClassed() {
            return isClassed;
        }

        public void setClassed(boolean isClassed) {
            this.isClassed = isClassed;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public Point() {
            x = 0;
            y = 0;
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(String str) {
            String[] p = str.split(",");
            this.x = Integer.parseInt(p[0]);
            this.y = Integer.parseInt(p[1]);
        }

        public String print() {
            return "<" + this.x + "," + this.y + ">";
        }
    }

    public static void main(String ags[]) {
        MainDBScan c = new MainDBScan();
        try {

            c.dbScan();
            c.display(hasilAkhirList);
        } catch (IOException e) {
            e.printStackTrace(System.out);

        }

    }
}
