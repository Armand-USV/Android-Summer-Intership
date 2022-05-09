package android.assist.tdl.ui.fragments.chart

import android.assist.tdl.R
import android.assist.tdl.classes.viewmodel.TaskViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.aachartmodel.aainfographics.aachartcreator.*

class ChartFragment : Fragment() {

    private lateinit var mTaskViewModel: TaskViewModel

    private lateinit var aaChartView : AAChartView

    private var toDoCounter : Int = 0
    private var inProgressCounter : Int = 0
    private var doneCounter : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_chart, container, false)

        aaChartView = view.findViewById(R.id.chartAA)

        val adapter = ChartAdapter()

        mTaskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        mTaskViewModel.readAllData.observe(viewLifecycleOwner) { task ->
            adapter.setData(task)
            toDoCounter = adapter.getToDoCount(task)
            inProgressCounter = adapter.getInProgressCount(task)
            doneCounter = adapter.getDoneCount(task)

            val aaChartModel : AAChartModel = AAChartModel()
                .chartType(AAChartType.Bar)
                .title("Graph 'To Do' vs 'In progress' vs 'Done'")
                .backgroundColor(R.color.white)
                .dataLabelsEnabled(true)
                .series(arrayOf(
                    AASeriesElement()
                        .name("To Do")
                        .data(arrayOf(toDoCounter)),
                    AASeriesElement()
                        .name("In progress")
                        .data(arrayOf(inProgressCounter)),
                    AASeriesElement()
                        .name("Done")
                        .data(arrayOf(doneCounter)),
                )
                )

            aaChartView.aa_drawChartWithChartModel(aaChartModel)

        }

        return view
    }
}