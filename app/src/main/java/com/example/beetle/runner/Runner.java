package com.example.beetle.runner;

import com.example.beetle.base.Pair;
import com.example.beetle.runner.Vector;

import java.util.List;

public interface Runner {

    List<Pair<Move, Vector>> run();

}
