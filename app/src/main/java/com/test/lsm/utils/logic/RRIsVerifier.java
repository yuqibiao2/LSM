package com.test.lsm.utils.logic;

import java.util.ArrayList;
import java.util.Iterator;

public class RRIsVerifier {
    private static Integer a = 6000;
    private static Integer b = 150;
    private Integer c = 0;
    private Integer d = 0;
    private ArrayList e = new ArrayList();
    private ArrayList f = new ArrayList();
    private Integer g;
    private boolean h;
    private Integer i;
    private ArrayList j;
    private boolean k;
    private int l;
    private int m;
    private int n;
    private int o;
    private int p;
    private int q;
    private Long r;

    public RRIsVerifier() {
        this.g = 0;
        this.h = false;
        this.i = 0;
        this.j = new ArrayList();
        this.k = true;
        this.l = 0;
        this.m = -1;
        this.n = 0;
        this.o = 0;
        this.p = -1;
        this.q = 0;
        this.r = 0L;
    }

    public int[] checkDisplayHR(int var1, int var2) {
        int var3 = var1;
        int var4 = var2;
        byte var5 = 0;
        if (var2 < 245 || var2 > 2100) {
            var4 = 0;
        }

        if (var4 > 0 && this.p > 0 && var2 == this.p) {
            ++this.q;
            Long var6;
            Long var7 = var6 = System.currentTimeMillis() / 1000L - this.r;
            if (this.r > 0L && var7 > 20L) {
                var5 = 1;
            } else if (this.r == 0L) {
                this.r = var6;
            }
        } else {
            this.q = 0;
            this.r = 0L;
        }

        this.p = var4;
        if (var5 != 0) {
            var4 = 0;
        }

        if (var1 > 0) {
            this.k = false;
        }

        if (var4 > 0) {
            this.j.add((double)var4);

            while(this.j.size() > 6) {
                this.j.remove(0);
            }
        }

        double var13 = 0.0D;
        if (this.j.size() > 0) {
            double var9;
            for(Iterator var8 = this.j.iterator(); var8.hasNext(); var13 += var9) {
                var9 = (Double)var8.next();
            }

            var13 /= (double)this.j.size();
        }

        double var14 = 0.0D;
        if (var4 > 0) {
            var14 = 60000.0D / var13;
        }

        if (var4 <= 0) {
            var3 = 0;
        }

        int var10 = var3;
        ++this.n;
        if (this.n > 6) {
            if (this.k) {
                this.h = true;
                var3 = (int)Math.ceil(var14);
            }

            if (var3 == 0) {
                ++this.o;
            } else {
                this.o = 0;
            }

            if (this.o > 60) {
                this.m = 0;
                this.o = 0;
            } else if (var3 >= 30 && var3 <= 240) {
                if (this.m != -1 && this.l > 60 && Math.abs(var3 - this.m) > 8) {
                    if (var3 - this.m > 0) {
                        this.m += 5;
                    } else {
                        this.m -= 5;
                    }
                } else {
                    this.m = var3;
                }
            }
        }

        this.l = var3;
        if (this.m != -1) {
            if (var10 <= 0 && var4 == 0) {
                this.g = this.g + 1;
            } else {
                if (this.g > 1 || var10 <= 0) {
                    this.h = true;
                }

                this.g = 0;
                double var11 = Math.abs((double)this.m - var14);
                if (this.h && !this.k && var11 < 10.0D) {
                    this.h = false;
                } else if (this.h && !this.k) {
                    this.m = (int)Math.ceil(var14);
                }
            }
        }

        return new int[]{this.m, var5};
    }

    public boolean feedOneRRI(int var1) {
        if (var1 >= 250 && var1 <= 2100) {
            this.i = this.i + 1;
            if (this.i > 6 && this.f.size() > 0 && Math.abs((Integer)this.f.get(this.f.size() - 1) - var1) > 600) {
                return false;
            } else {
                boolean var2 = false;
                this.c = this.c + var1;
                this.f.add(var1);
                if (this.c > a) {
                    Integer var9 = -1;
                    Integer var3 = 0;
                    ArrayList var4 = new ArrayList();
                    Integer var5 = 0;

                    for(Iterator var7 = this.f.iterator(); var7.hasNext(); var5 = var5 + 1) {
                        Integer var8 = (Integer)var7.next();
                        if (var9 > 0 && Math.abs(var9 - var8) > b) {
                            var3 = var3 + 1;
                            var4.add(this.d + var5);
                        }

                        var9 = var8;
                    }

                    if (var3 > 1 && !a(this.e, var4) && a(this.f) > 30.0D) {
                        this.e = var4;
                        var2 = true;
                    }

                    this.c = this.c - (Integer)this.f.remove(0);
                    this.d = this.d + 1;
                }

                return var2;
            }
        } else {
            return false;
        }
    }

    private static double a(ArrayList var0) {
        double var1 = 0.0D;
        double var3 = 0.0D;
        int var5 = var0.size();

        Integer var7;
        for(Iterator var6 = var0.iterator(); var6.hasNext(); var1 += Double.valueOf((double)var7)) {
            var7 = (Integer)var6.next();
        }

        double var10 = var1 / (double)var5;

        Integer var9;
        for(Iterator var8 = var0.iterator(); var8.hasNext(); var3 += (Double.valueOf((double)var9) - var10) * (Double.valueOf((double)var9) - var10)) {
            var9 = (Integer)var8.next();
        }

        return Math.sqrt(var3 / (double)var5);
    }

    private static boolean a(ArrayList var0, ArrayList var1) {
        if (var0.size() == var1.size()) {
            for(Integer var2 = 0; var2 < var0.size(); var2 = var2 + 1) {
                if (var0.get(var2) != var1.get(var2)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }
}